import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(@NotNull String[] args) {
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
        for (String line: inputLines) {
            simulator.simulate(line);
        }
    }

    @NotNull
    private static String[] readFile(String FileName) {
        Path file = Paths.get(FileName);
        Charset charset = StandardCharsets.US_ASCII;
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return list.toArray(new String[0]);
    }
}
