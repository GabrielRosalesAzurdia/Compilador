public class Symbol {
    private final String name;
    private final String type;
    private final String scope;
    private final String role;

    public Symbol(String name, String type, String scope, String role) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Nombre: " + name + ", Tipo: " + type + ", Ámbito: " + scope + ", Rol: " + role;
    }
}
