package domain;

import java.util.ArrayList;
import java.util.Objects;

public class HashTable {
    private final double THRESHOLD = 0.7;
    private ArrayList<HashNode> array; // each slot contains the head of a linked list
    private int capacity;
    private int size;


    public HashTable() {
        array = new ArrayList<>();
        capacity = 10;
        size = 0;

        // Create empty chains
        for (int i = 0; i < capacity; i++)
            array.add(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }


    // Adds a key value pair to hash
    public void add(Object value) {
        // Find head of chain for given key
        int index = hashCode(value);
        HashNode head = array.get(index);

        // special case for head
        if (head == null) {
            array.set(index, new HashNode(index, value));
            return;
        }

        // Check if key is already present
        while (head.next != null) {
            if (head.value.equals(value))
                return;

            head = head.next;
        }

        if (head.value.equals(value))
            return;


        // Insert key at the beginning of the chain if it is not present
        head.next = new HashNode(index, value);
        size++;

        // If load factor goes beyond threshold, then double hash table size
        if (loadFactor() >= THRESHOLD)
            resize();

    }


    // Returns value for a key
    public Object get(Integer key) {
        // Find head of chain for given key
        int index = hashCode(key);

        HashNode head = array.get(index);

        // Search key in chain
        while (head != null) {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }

    private void resize() {
        ArrayList<HashNode> temp = array;
        array = new ArrayList<>();
        capacity = 2 * capacity;
        size = 0;

        for (int i = 0; i < capacity; i++)
            array.add(null);

        for (HashNode headNode : temp) {
            while (headNode != null) {
                this.add(headNode.value);
                headNode = headNode.next;
            }
        }
    }

    private Integer hashCode(Object value) {
        // char sum % capacity
        long sum = 0;
        String string = value.toString();
        for (int i = 0; i < string.length(); i++) {
            sum += string.charAt(i);
        }

        // Storing the sum of last word
        return (int) (sum % capacity);
    }

    private double loadFactor() {
        return (1.0 * size) / capacity;
    }

    @Override
    public String toString() {
        return "HashTable{" +
                "THRESHOLD=" + THRESHOLD +
                ", array=" + array +
                ", capacity=" + capacity +
                ", size=" + size +
                '}';
    }
}
