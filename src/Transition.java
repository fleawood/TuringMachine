public class Transition {
    private String oldState;
    private String newState;
    private char oldSymbol;
    private char newSymbol;
    private int dir;

    public Transition() {

    }

    public Transition(String oldState, char oldSymbol, String newState, char newSymbol, int dir) {
        this.oldState = oldState;
        this.oldSymbol = oldSymbol;
        this.newState = newState;
        this.newSymbol = newSymbol;
        this.dir = dir;
    }
}
