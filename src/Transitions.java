import java.util.HashMap;

public class Transitions {
    private HashMap<TransOldArgs, TransNewArgs> trans;

    Transitions() {
        trans = new HashMap<>();
    }

    void addNewTransition(String oldState, char oldSymbol, String newState, char newSymbol, int dir) {
        TransOldArgs oldArgs = new TransOldArgs(oldState, oldSymbol);
        TransNewArgs newArgs = new TransNewArgs(newState, newSymbol, dir);
//        System.out.println(oldArgs.hashCode());
        trans.put(oldArgs, newArgs);
    }

    TransNewArgs getNextArgs(String currentState, Character currentSymbol) {
        TransOldArgs oldArgs = new TransOldArgs(currentState, currentSymbol);
//        System.out.println(oldArgs.hashCode());
        return trans.get(oldArgs);
    }
}
