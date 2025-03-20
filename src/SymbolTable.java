import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Symbol> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void addSymbol(String name, String type, String scope, String role) {
        if (!table.containsKey(name)) {
            table.put(name, new Symbol(name, type, scope, role));
        }
    }

    public Symbol getSymbol(String name) {
        return table.get(name);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tabla de Símbolos:\n");
        for (Symbol symbol : table.values()) {
            sb.append(symbol).append("\n");
        }
        return sb.toString();
    }
}
