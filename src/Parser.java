import java.util.HashSet;

class Parser {
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

    private void error(String line) throws SyntaxException {
        String message = String.format("Error in `%s`%n", line);
        throw new SyntaxException(message);
    }

    private String cropComment(String string) {
        int k = string.indexOf(';');
        if (k == -1) return string;
        return string.substring(0, k);
    }

    private int getNextLineIndex(String[] lines, int i) {
        while (i < lines.length) {
            lines[i] = cropComment(lines[i]).replaceAll("\\s+$", "");
            if (lines[i].isEmpty()) {
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
        if (strings.length != 5) {
            throw new RuntimeException();
        }

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

    private void readStateSet(String line) throws SyntaxException, IndexOutOfBoundsException {
        String[] strings = getSetContent(line, "Q");
        getOneString(states, strings);
    }

    private void readInputSymbols(String line) {
        String[] strings = getSetContent(line, "S");
        getOneChar(inputSymbols, strings);
    }

    private void readSymbols(String line) {
        String[] strings = getSetContent(line, "T");
        getOneChar(symbols, strings);
    }

    private void readInitState(String line) {
        String string = getContent(line, "q0");
        if (!states.contains(string)) error(line);
        initState = string;
    }

    private void readBlankSymbol(String line) {
        String string = getContent(line, "B");
        if (string.length() != 1) error(line);
        blankSymbol = string.charAt(0);
    }

    private void readFinalSymbols(String line) {
        String[] strings = getSetContent(line, "F");
        getOneString(finalStates, strings);
    }

    private void readTransFunc(String line) {
        String[] strings = line.split(" ");
        addTrans(strings);
    }

    Simulator parse(String[] lines) {
        int i = 0;
        while (i < lines.length) {
            i = getNextLineIndex(lines, i);
            if (i == -1) break;
            String line = lines[i];
            if (line.startsWith("#Q")) readStateSet(line);
            else if (line.startsWith("#S")) readInputSymbols(line);
            else if (line.startsWith("#T")) readSymbols(line);
            else if (line.startsWith("#q0")) readInitState(line);
            else if (line.startsWith("#B")) readBlankSymbol(line);
            else if (line.startsWith("#F")) readFinalSymbols(line);
            else readTransFunc(line);
            i++;
        }
        return new Simulator(inputSymbols, initState, blankSymbol, finalStates, transitions);
    }
}
