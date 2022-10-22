package domain;

public class HashNode {
    Integer key;
    Object value;

    // Reference to next node
    HashNode next;

    // Constructor
    public HashNode(Integer key, Object value) {
        this.key = key; // hashcode
        this.value = value;
    }

    @Override
    public String toString() {
        return "HashNode{" +
                "key=" + key +
                ", value=" + value +
                ", next=" + next +
                '}';
    }
}