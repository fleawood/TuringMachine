import java.util.AbstractMap;
import java.util.Map;

public class TransOldArgs {
    private Map.Entry<String, Character> data;

    TransOldArgs(String state, char symbol) {
        data = new AbstractMap.SimpleImmutableEntry<>(state, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TransOldArgs)) return false;
        return data.equals(((TransOldArgs) obj).data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    String getState() {
        return data.getKey();
    }

    Character getSymbol() {
        return data.getValue();
    }
}
