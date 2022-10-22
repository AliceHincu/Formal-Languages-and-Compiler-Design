import domain.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable ST = new SymbolTable();

        ST.add("ad");
        ST.add("bc");
        System.out.println(ST);
    }
}