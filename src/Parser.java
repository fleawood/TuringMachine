import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.StringTokenizer;

public class Parser {
    private HashSet<String> states;

    public Parser() {
        states = new HashSet<>();
    }

    private void error(@NotNull String[] lines, @NotNull Integer i) throws SyntaxException {
        String message = String.format("Error in line `%s`%n", lines[i]);
        throw new SyntaxException(message);
    }

    private void readStateSet(String[] lines, Integer i) throws SyntaxException, IndexOutOfBoundsException {
        while (lines[i].isEmpty()) i++;
        String line = lines[i];
        if (!line.startsWith("#Q = {") || !line.endsWith("}")) error(lines, i);
        line = line.substring(6, line.length() - 1);
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        while (tokenizer.hasMoreTokens()) {
            String state = tokenizer.nextToken();
            if (states.contains(state)) {
                error(lines, i);
            } else {
                states.add(state);
            }
        }
    }

    public void parse(String[] lines) {
        Integer i = 0;
        try {
            readStateSet(lines, i);
        } catch (SyntaxException, IndexOutOfBoundsException) {

        }
    }

    public static void main(String[] args) {
        System.out.println("You are debugging `Parser`.");
    }
}
