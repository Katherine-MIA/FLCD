//Documentation -> hashTable
//Documentation -> Analyzer
//Homework 1 a)

public class Main {
    public static void main(String[] args) {
        String inputFileName  = "src/data/input.txt";
        String atomsFileName  = "src/data/atoms.txt";
        String outputFileName = "src/data/output.txt";
        String tokensFileName = "src/data/tokens.txt";
        String tsFileName     = "src/data/ts.txt";
        String fipFileName    = "src/data/pif.txt";

        Analyzer analyzer = new Analyzer(inputFileName, atomsFileName, outputFileName, tokensFileName, tsFileName, fipFileName);

        try
        {
            analyzer.readAtomsFromFile();
            analyzer.readTokensFromFile();
            analyzer.readInputFromFile();
            //analyzer.obtainSymbols();
            analyzer.obtainSymbolsV2();
            analyzer.writeOutputToFile();
            analyzer.writeToTsFile();
            analyzer.writeToFipFile();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
