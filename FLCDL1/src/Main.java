import java.io.IOException;


//Documentaation -> hashTable
//Documentation -> Analyzer
//Homework 1 a)

public class Main {
    public static void main(String[] args) {
        String inputFileName  = "date/input.txt";
        String atomsFileName  = "date/atoms.txt";
        String outputFileName = "date/output.txt";
        String tokensFileName = "date/tokens.txt";
        String tsFileName     = "date/ts.txt";
        String fipFileName    = "date/pif.txt";

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
