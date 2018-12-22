import java.util.HashMap;

class Transitions {
    private HashMap<TransOldArgs, TransNewArgs> trans;
    private final static Character wildcard = '*';

    Transitions() {
        trans = new HashMap<>();
    }

    void addNewTransition(String oldState, char oldSymbol, String newState, char newSymbol, int dir) {
        TransOldArgs oldArgs = new TransOldArgs(oldState, oldSymbol);
        TransNewArgs newArgs = new TransNewArgs(newState, newSymbol, dir);
        trans.put(oldArgs, newArgs);
    }

    TransNewArgs getTrans(TransOldArgs oldArgs) {
        return trans.get(oldArgs);
    }

    TransNewArgs getNextArgs(String currentState, Character currentSymbol) {
        TransNewArgs res = trans.get(new TransOldArgs(currentState, currentSymbol));
        return res == null ? trans.get(new TransOldArgs(currentState, wildcard)) : res;
    }
}
