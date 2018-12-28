import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    private static final String consoleFileName = "console.txt";
    private static final String resultFileName = "result.txt";
    private static final String testTMFileName = "test.tm";
    private static final String inputFileName = "input.txt";

    private static PrintWriter getPrintWriter(Path file) {
        Charset charset = StandardCharsets.US_ASCII;
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(file, charset);
            return new PrintWriter(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.printf("Expect 1 argument, but get %d argument(s).", args.length);
            System.exit(1);
        }
        String caseDir = args[0];
        Path TMFile = Paths.get(caseDir, testTMFileName);
        Path inputFile = Paths.get(caseDir, inputFileName);

        String[] tmLines = readFile(TMFile);
        Parser parser = new Parser();
        Simulator simulator = parser.parse(tmLines);

        String[] inputLines = readFile(inputFile);

        Path consoleFile = Paths.get(caseDir, consoleFileName);
        Path resultFile = Paths.get(caseDir, resultFileName);

        simulator.setConsoleOutput(getPrintWriter(consoleFile));
        simulator.setResultOutput(getPrintWriter(resultFile));

        for (String line : inputLines) {
            simulator.simulate(line);
        }
        simulator.closeOutput();
    }

    private static String[] readFile(Path file) {
        Charset charset = StandardCharsets.US_ASCII;
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }
}
