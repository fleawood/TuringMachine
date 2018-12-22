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

    private static PrintWriter getPrintWriter(String fileName) {
        Path file = Paths.get(fileName);
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
        if (args.length != 2) {
            System.out.println("Error");
            return;
        }
        String tmFileName = args[0];
        String inputFileName = args[1];

        String[] tmLines = readFile(tmFileName);
        Parser parser = new Parser();
        Simulator simulator = parser.parse(tmLines);

        String[] inputLines = readFile(inputFileName);

        simulator.setConsoleOutput(getPrintWriter(consoleFileName));
        simulator.setResultOutput(getPrintWriter(resultFileName));

        for (String line : inputLines) {
            simulator.simulate(line);
        }
        simulator.closeOutput();
    }

    private static String[] readFile(String fileName) {
        Path file = Paths.get(fileName);
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
