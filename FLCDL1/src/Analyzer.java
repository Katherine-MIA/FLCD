import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analyzer {

    private String inputFileName;
    private String atomsFileName;
    private String outputFileName;
    private String tokensFileName;
    private String tsFileName;
    private String fipFileName;
    private List<String> fileLines;
    private List<String> atoms;
    private List<Pair<String, Integer>> symbols;
    private List<Pair<String, Integer>> tokens;
    private HashTable identifiers;
    private HashTable constants;

    Analyzer(String inputFileName, String atomsFileName, String outputFileName, String tokensFileName, String tsFileName, String fipFileName)
    {
        this.inputFileName  = inputFileName;
        this.atomsFileName  = atomsFileName;
        this.outputFileName = outputFileName;
        this.tokensFileName = tokensFileName;
        this.tsFileName     = tsFileName;
        this.fipFileName    = fipFileName;
        this.fileLines      = new ArrayList<>();
        this.symbols        = new ArrayList<>();
        this.atoms          = new ArrayList<>();
        this.tokens         = new ArrayList<>();
        this.identifiers    = new HashTable();
        this.constants      = new HashTable();
    }

    void readAtomsFromFile() throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(atomsFileName));
        String line = bufferedReader.readLine();
        atoms = Arrays.asList(line.split(" "));

        bufferedReader.close();
    }

    void readTokensFromFile() throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(tokensFileName));
        String line = bufferedReader.readLine();

        int tokenCode = 1;
        for (String token : line.split(" "))
        {
            tokens.add(new Pair<>(token, tokenCode));
            tokenCode += 1;
        }

        bufferedReader.close();
    }

    void readInputFromFile() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        String line;

        while ((line = br.readLine()) != null) {
            fileLines.add(line);
        }

        br.close();
    }

    void writeOutputToFile() throws IOException
    {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8));

        for (Pair<String, Integer> symbol : symbols)
        {
            String result = symbol.getFirst() + " " + symbol.getSecond() + "\n";
            writer.write(result);
        }

        writer.close();
    }

    void writeToTsFile() throws IOException
    {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tsFileName), StandardCharsets.UTF_8));

        writer.write("Cod               Simbol\n");
        List<String> entriesIdentifiers = identifiers.getEntries();
        for (int index = 0; index < entriesIdentifiers.size(); ++index)
        {
            if (entriesIdentifiers.get(index) != null)
            {
                int spaceDiference = 3 - entriesIdentifiers.get(index).length();
                writer.write(entriesIdentifiers.get(index));
                for (int space = 0; space < spaceDiference + 15; ++space)
                {
                    writer.write(" ");
                }

                writer.write(index + "\n");
            }
        }
        writer.write("\nCod               Simbol\n");
        List<String> entriesConstants = constants.getEntries();
        for (int index = 0; index < entriesConstants.size(); ++index)
        {
            if (entriesConstants.get(index) != null)
            {
                int spaceDiference = 3 - entriesConstants.get(index).length();
                writer.write(entriesConstants.get(index));
                for (int space = 0; space < spaceDiference + 15; ++space)
                {
                    writer.write(" ");
                }

                writer.write(index + "\n");
            }
        }

        writer.close();
    }

    void writeToFipFile() throws IOException
    {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fipFileName), StandardCharsets.UTF_8));

        writer.write("Atom lexical               Cod atom               Cod TS\n");
        for (Pair<String, Integer> symbol : symbols) {
            String result = "";
            if (1 != symbol.getSecond() && 2 != symbol.getSecond()) {
                result = symbol.getFirst() + "                         " + symbol.getSecond() + "                         " + "-" + "\n";
            } else {
                int index = identifiers.findElement(symbol.getFirst());
                if (index == -1) {
                    index = constants.findElement(symbol.getFirst());
                }
                result = symbol.getFirst() + "                         " + symbol.getSecond() + "                        " + index + "\n";
            }

            writer.write(result);
        }

        writer.close();
    }

    private boolean checkIfAtomIsIdentifier(String atom)
    {
        return atom.chars().allMatch(Character::isLetter);
    }

    private boolean checkIfAtomIsConstant(String atom)
    {
        return atom.chars().allMatch(Character::isDigit);
    }

    public void obtainSymbols() throws Exception
    {
        int indexLine = 1;
        for (String line : fileLines)
        {
            char[] lineCharArray = line.toCharArray();
            int nrUsedChars = 0;

            for (int indexChar = 0; indexChar < lineCharArray.length; ++indexChar)
            {
                for (int indexAtom = 0; indexAtom < atoms.size(); ++indexAtom)
                {
                    boolean gasit = true;

                    int indexCharActual = indexChar;
                    int identicChars = 0;

                    for (char charAtom : atoms.get(indexAtom).toCharArray())
                    {
                        if (indexCharActual < lineCharArray.length && lineCharArray[indexCharActual] != charAtom)
                        {
                            gasit = false;
                        }
                        else
                        {
                            if (indexCharActual < lineCharArray.length)
                            {
                                identicChars++;
                            }
                        }

                        indexCharActual++;
                    }

                    if (identicChars != atoms.get(indexAtom).toCharArray().length) {
                        gasit = false;
                    }

                    if (gasit)
                    {
                        nrUsedChars += atoms.get(indexAtom).length();

                        String atom = atoms.get(indexAtom);
                        boolean isToken = false;
                        for (Pair<String, Integer> token : tokens)
                        {
                            if (token.getFirst().equals(atom))
                            {
                                isToken = true;
                                symbols.add(new Pair<>(atom, token.getSecond()));
                            }
                        }
                        if (!isToken)
                        {
                            if (checkIfAtomIsIdentifier(atom))
                            {
                                if (atom.toCharArray().length > 250)
                                    throw new Exception("The identifier can't have more than 250 characters!");
                                symbols.add(new Pair<>(atom, 1));
                                identifiers.add(atom);
                            }
                            else if (checkIfAtomIsConstant(atom))
                            {
                                symbols.add(new Pair<>(atom, 2));
                                constants.add(atom);
                            }
                        }

                        indexAtom = atoms.size();
                        indexChar = indexCharActual - 1;
                    }
                }

                if (lineCharArray[indexChar] == ' ' || lineCharArray[indexChar] == '\t')
                {
                    nrUsedChars += 1;
                }
            }

            if (nrUsedChars != lineCharArray.length)
            {
                throw new Exception("Eroare lexicala la linia " + indexLine + " " + nrUsedChars + " " + lineCharArray.length + "     linia contine: " + line + "\n" );
            }
            indexLine += 1;
        }
    }

    private void cleanSpacesFromAtoms(String[] atoms)
    {
        for (int indexAtom = 0; indexAtom < atoms.length; ++indexAtom)
        {
            StringBuilder newAtom = new StringBuilder();
            char[] atomsChar = atoms[indexAtom].toCharArray();
            for (char character : atomsChar)
            {
                if (character != ' ' && character != '\t')
                {
                    newAtom.append(character);
                }
            }
            atoms[indexAtom] = newAtom.toString();
        }
    }

    void obtainSymbolsV2() throws Exception
    {
        int indexLine = 0;
        for (String line : fileLines)
        {
            if (line.length() != 0)
            {
                String[] atoms = line.split(" ");
                cleanSpacesFromAtoms(atoms);
                int nrAtomsUsed = 0;
                for (String atom : atoms)
                {
                    boolean posibleIdOrConst = true;
                    for (Pair<String,Integer> token : tokens)
                    {
                        if (token.getFirst().equals(atom))
                        {
                            nrAtomsUsed += 1;
                            posibleIdOrConst = false;
                            symbols.add(new Pair<>(atom, token.getSecond()));
                        }
                    }

                    if (posibleIdOrConst)
                    {
                        if (checkIfAtomIsIdentifier(atom))
                        {
                            if (atom.toCharArray().length > 250)
                                throw new Exception("The identifier can't have more than 250 characters!");
                            nrAtomsUsed += 1;
                            symbols.add(new Pair<>(atom, 1));
                            identifiers.add(atom);
                        }
                        else if (checkIfAtomIsConstant(atom))
                        {
                            nrAtomsUsed += 1;
                            symbols.add(new Pair<>(atom, 2));
                            constants.add(atom);
                        }
                    }
                }
                if (nrAtomsUsed != atoms.length)
                {
                    throw new Exception("Eroare lexicala la linia " + indexLine + "     linia contine: " + line + "\n");
                }
            }
            indexLine += 1;
        }
    }

    public List<String> getLines()
    {
        return fileLines;
    }

    public List<Pair<String, Integer>> getSymbols()
    {
        return symbols;
    }

    public List<String> getAtoms()
    {
        return atoms;
    }

    public List<Pair<String, Integer>> getTokens()
    {
        return tokens;
    }
}
