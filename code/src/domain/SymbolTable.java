package domain;

public class SymbolTable {
    HashTable ST = new HashTable();
    int ST_POS = 0;

    public void add(Object key) {
        ST.put(key, ST_POS++);
    }

    public void get(Object key) {
        ST.get(key);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "ST=" + ST +
                ", ST_POS=" + ST_POS +
                '}';
    }
}
