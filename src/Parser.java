import java.util.HashSet;

public class Parser {
    private HashSet<String> states;
    private HashSet<Character> symbols;
    private HashSet<Character> inputSymbols;
    private String initState;
    private Character blankSymbol;
    private HashSet<String> finalStates;
    private Transitions transitions;

    Parser() {
        states = new HashSet<>();
        symbols = new HashSet<>();
        inputSymbols = new HashSet<>();
        finalStates = new HashSet<>();
        transitions = new Transitions();
    }

    public static void main(String[] args) {
        System.out.println("You are debugging `Parser`.");
    }

    private void error(String[] lines, Integer i) throws SyntaxException {
        String message = String.format("Error in line `%s`%n", lines[i]);
        throw new SyntaxException(message);
    }

    private String cropComment(String string) {
        int k = string.indexOf(';');
        if (k == -1) return string;
        return string.substring(0, k);
    }

    private int getNextLineIndex(String[] lines, int i) {
        while (i < lines.length) {
            String line = cropComment(lines[i]).replaceAll("\\s+$", "");
            if (line.isEmpty()) {
                i++;
                continue;
            }
            return i;
        }
        return -1;
    }

    private String[] getSetContent(String line, String s) throws SyntaxException {
        if (line == null) throw new SyntaxException();
        if (!line.startsWith("#" + s + " = {") || !line.endsWith("}")) throw new SyntaxException();
        line = line.substring(5 + s.length(), line.length() - 1);
        return line.split(",");
    }

    private String getContent(String line, String s) {
        if (line == null) throw new SyntaxException();
        if (!line.startsWith("#" + s + " = ")) throw new SyntaxException();
        return line.substring(4 + s.length());
    }

    private void getOneString(HashSet<String> set, String[] strings) {
        for (String string : strings) {
            if (set.contains(string)) {
                throw new SyntaxException();
            } else {
                set.add(string);
            }
        }
    }

    private void getOneChar(HashSet<Character> set, String[] strings) {
        for (String string : strings) {
            Character symbol = string.charAt(0);
            if (set.contains(symbol)) {
                throw new SyntaxException();
            } else {
                set.add(symbol);
            }
        }
    }

    private void addTrans(String[] strings) {
        assert strings.length == 5;

        String oldState, newState;
        char oldSymbol, newSymbol;
        int dir = 0;

        oldState = strings[0];
        oldSymbol = strings[1].charAt(0);
        newSymbol = strings[2].charAt(0);

        switch (strings[3].charAt(0)) {
            case 'l':
                dir = -1;
                break;
            case 'r':
                dir = 1;
                break;
            case '*':
                dir = 0;
                break;
        }
        newState = strings[4];
        transitions.addNewTransition(oldState, oldSymbol, newState, newSymbol, dir);
    }

    private int readStateSet(String[] lines, int i) throws SyntaxException, IndexOutOfBoundsException {
        int index = getNextLineIndex(lines, i);
        String[] strings = getSetContent(lines[index], "Q");
        getOneString(states, strings);
        return index;
    }

    private int readInputSymbols(String[] lines, int i) {
        int index = getNextLineIndex(lines, i);
        String[] strings = getSetContent(lines[index], "S");
        getOneChar(inputSymbols, strings);
        return index;
    }

    private int readSymbols(String[] lines, int i) {
        int index = getNextLineIndex(lines, i);
        String[] strings = getSetContent(lines[index], "T");
        getOneChar(symbols, strings);
        return index;
    }

    private int readInitState(String[] lines, int i) {
        int index = getNextLineIndex(lines, i);
        String string = getContent(lines[index], "q0");
        if (!states.contains(string)) error(lines, i);
        initState = string;
        return index;
    }

    private int readBlankSymbol(String[] lines, int i) {
        int index = getNextLineIndex(lines, i);
        String string = getContent(lines[index], "B");
        if (string.length() != 1) error(lines, i);
        blankSymbol = string.charAt(0);
        return index;
    }

    private int readFinalSymbols(String[] lines, int i) {
        int index = getNextLineIndex(lines, i);
        String[] strings = getSetContent(lines[index], "F");
        getOneString(finalStates, strings);
        return index;
    }

    private int readTransFunc(String[] lines, int i) {
        while (i < lines.length) {
            i = getNextLineIndex(lines, i);
            String line = lines[i];
            if (line == null) break;
            String[] strings = line.split(" ");
            addTrans(strings);
            i = i + 1;
        }
        return i;
    }

    Simulator parse(String[] lines) {
        int i = 0;
        i = readStateSet(lines, i);
        i = readInputSymbols(lines, i + 1);
        i = readSymbols(lines, i + 1);
        i = readInitState(lines, i + 1);
        i = readBlankSymbol(lines, i + 1);
        i = readFinalSymbols(lines, i + 1);
        i = readTransFunc(lines, i + 1);

        if (i != lines.length) throw new RuntimeException();
        return new Simulator(inputSymbols, initState, blankSymbol, finalStates, transitions);
    }
}
