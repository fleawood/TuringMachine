import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;


class Simulator {
    private final static Character wildcard = '*';
    private HashSet<Character> inputSymbols;
    private String initState;
    private Character blankSymbol;
    private HashSet<String> finalStates;
    private Transitions transitions;
    private String currentState;
    private ArrayList<Character> tapePos, tapeNeg;
    private int head;
    private int num_steps;
    private PrintWriter consoleOutput;
    private PrintWriter resultOutput;

    Simulator(HashSet<Character> inputSymbols,
              String initState,
              Character blankSymbol,
              HashSet<String> finalStates,
              Transitions transitions) {

        this.inputSymbols = inputSymbols;
        this.initState = initState;
        this.blankSymbol = blankSymbol;
        this.finalStates = finalStates;
        this.transitions = transitions;

        tapePos = new ArrayList<>();
        tapeNeg = new ArrayList<>();

        consoleOutput = new PrintWriter(System.out);
        resultOutput = new PrintWriter(System.out);
    }

    private void resetTape(String string) {
        tapePos.clear();
        tapeNeg.clear();
        for (Character c : string.toCharArray()) {
            tapePos.add(c);
        }
        if (tapePos.isEmpty()) {
            tapePos.add(blankSymbol);
        }
        tapeNeg.add(blankSymbol);
    }

    private void printEnd() {
        consoleOutput.println("==================== END ====================");
    }

    private void printIllegal(String string) {
        consoleOutput.println("==================== ERR ====================");
        consoleOutput.printf("The input \"%s\" is illegal%n", string);

    }

    private void printRun() {
        consoleOutput.println("==================== RUN ====================");
    }

    private void printInput(String string) {
        consoleOutput.printf("Input: %s%n", string);
    }

    private void printStep() {
        consoleOutput.printf("Step  : %d%n", num_steps);
    }

    private void appendExtraBlanks(StringBuilder builder, int k) {
        for (int i = 0; i < k; i++) {
            builder.append(' ');
        }
    }

    private String[] printSingleTape(int l, int r, boolean neg) {
        StringBuilder indexBuilder = new StringBuilder();
        StringBuilder tapeBuilder = new StringBuilder();
        StringBuilder headBuilder = new StringBuilder();

        ArrayList<Character> tape = neg ? tapeNeg : tapePos;
        for (int i = l; i <= r; i++) {
            int index = neg ? -i : i;
            String str = String.format("%d", index);
            indexBuilder.append(str).append(' ');

            tapeBuilder.append(tape.get(index)).append(' ');
            appendExtraBlanks(tapeBuilder, str.length() - 1);

            headBuilder.append(head == index ? '^' : ' ').append(' ');
            appendExtraBlanks(headBuilder, str.length() - 1);
        }
        String indexString = indexBuilder.toString();
        String tapeString = tapeBuilder.toString();
        String headString = headBuilder.toString();
        return new String[]{indexString, tapeString, headString};
    }

    private String[] printTapeRange(int l, int r) {
        assert l <= r;
        if (r < 0) {
            return printSingleTape(l, r, true);
        } else if (l >= 0) {
            return printSingleTape(l, r, false);
        } else {
            String[] stringsLeft = printSingleTape(l, -1, true);
            String[] stringsRight = printSingleTape(0, r, false);
            String[] strings = new String[3];
            for (int i = 0; i < 3; ++i) {
                strings[i] = stringsLeft[i] + stringsRight[i];
            }
            return strings;
        }
    }

    private String getStringRepr(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character c : list) {
            builder.append(c);
        }
        return builder.toString();
    }

    private String getTapeString() {
        return new StringBuilder(getStringRepr(tapeNeg)).deleteCharAt(0).reverse().toString()
                + getStringRepr(tapePos);
    }

    private void printTape() {
        String string = getTapeString();
        final int indexLowerBound = -tapeNeg.size() + 1;
        final int indexUpperBound = tapePos.size() - 1;

        if (indexUpperBound - indexLowerBound != string.length() - 1) throw new RuntimeException();

        int indexLeft = indexUpperBound, indexRight = indexLowerBound;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != blankSymbol) {
                indexLeft = indexLowerBound + i;
                break;
            }
        }
        for (int i = string.length() - 1; i >= 0; --i) {
            if (string.charAt(i) != blankSymbol) {
                indexRight = indexLowerBound + i;
                break;
            }
        }

        if (head < indexLeft) indexLeft = head;
        if (head > indexRight) indexRight = head;

        String[] strings = printTapeRange(indexLeft, indexRight);
        String indexString = "Index : " + strings[0].replaceAll("\\s+$", "");
        String headString = "Tape  : " + strings[1].replaceAll("\\s+$", "");
        String tapeString = "Head  : " + strings[2].replaceAll("\\s+$", "");

        consoleOutput.println(indexString);
        consoleOutput.println(headString);
        consoleOutput.println(tapeString);
    }

    private void printState() {
        consoleOutput.printf("State : %s%n", currentState);
    }

    private void printDividingLine() {
        consoleOutput.println("---------------------------------------------");
    }


    private void printDetails() {
        printStep();
        printTape();
        printState();
        printDividingLine();
    }

    private void printResult() {
        String string = getTapeString();
        String regexFirst = "^" + blankSymbol + "+";
        String regexLast = blankSymbol + "+$";
        string = string.replaceAll(regexFirst, "").replaceAll(regexLast, "");
        consoleOutput.printf("Result: %s%n", string);
    }

    private boolean checkInput(String string) {
        for (char ch : string.toCharArray()) {
            if (!inputSymbols.contains(ch)) {
                return false;
            }
        }
        return true;
    }

    private void ensureSize(ArrayList<Character> tape, int size) {
        tape.ensureCapacity(size);
        while (tape.size() < size) {
            tape.add(blankSymbol);
        }
    }

    private Character getCurrentSymbol() {
        if (head < 0) {
            ensureSize(tapeNeg, -head + 1);
            return tapeNeg.get(-head);
        } else {
            ensureSize(tapePos, head + 1);
            return tapePos.get(head);
        }
    }

    private void setCurrentSymbol(Character c) {
        if (c == wildcard) return;
        if (head < 0) {
            tapeNeg.set(-head, c);
        } else {
            tapePos.set(head, c);
        }
    }

    private void setCurrentState(String state) {
        currentState = state;
    }

    private void resetCurrentHead() {
        head = 0;
    }

    private void moveCurrentHead(int dir) {
        head += dir;
        getCurrentSymbol();
    }

    void simulate(String string) {
        printInput(string);
        if (!checkInput(string)) {
            printIllegal(string);
            printEnd();
            resultOutput.println("Error");
            return;
        }
        printRun();

        num_steps = 0;
        resetCurrentHead();
        setCurrentState(initState);
        resetTape(string);
        boolean result;
        while (true) {
            printDetails();
            if (finalStates.contains(currentState)) {
                result = true;
                break;
            }
            Character currentSymbol = getCurrentSymbol();
            TransNewArgs newArgs = transitions.getNextArgs(currentState, currentSymbol);
            if (newArgs == null) {
                result = false;
                break;
            }
            setCurrentState(newArgs.getState());
            setCurrentSymbol(newArgs.getSymbol());
            moveCurrentHead(newArgs.getDir());
            num_steps++;
        }
        printResult();
        printEnd();
        if (result) {
            resultOutput.println("Accept");
        } else {
            resultOutput.println("Reject");
        }
    }

    void setConsoleOutput(PrintWriter printWriter) {
        consoleOutput = printWriter;
    }

    void setResultOutput(PrintWriter printWriter) {
        resultOutput = printWriter;
    }

    void closeOutput() {
        consoleOutput.close();
        resultOutput.close();
    }
}
