import java.util.AbstractMap;
import java.util.Map;

public class TransNewArgs {
    private Map.Entry<Map.Entry<String, Character>, Integer> data;

    TransNewArgs(String state, char symbol, int dir) {
        data = new AbstractMap.SimpleImmutableEntry<>(new AbstractMap.SimpleImmutableEntry<>(state, symbol), dir);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TransNewArgs)) return false;
        return data.equals(((TransNewArgs) obj).data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    String getState() {
        return data.getKey().getKey();
    }

    Character getSymbol() {
        return data.getKey().getValue();
    }

    int getDir() {
        return data.getValue();
    }
}
