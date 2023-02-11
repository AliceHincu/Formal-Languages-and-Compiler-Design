package domain;

import java.util.*;

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


    /**
     * Maps the specified key to the specified value in this hashtable
     * If key is already present, returns the position of existing element. If not, returns the new position.
     *
     * @param key   - the identifier / constant
     * @param value - the position in the Symbol Table
     */
    public Integer put(Object key, Integer value) {
        // Find head of chain for given key
        int index = hashCode(key);
        HashNode head = array.get(index);

        // special case for head
        if (head == null) {
            array.set(index, new HashNode(key, value));
            return value;
        }

        // Check if key is already present
        while (head.next != null) {
            if (head.key.equals(key))
                return head.value;

            head = head.next;
        }

        if (head.key.equals(key))
            return head.value;


        // Insert key at the beginning of the chain if it is not present
        head.next = new HashNode(key, value);
        size++;

        // If load factor goes beyond threshold, then double hash table size
        if (loadFactor() >= THRESHOLD)
            resize();

        return value;
    }


    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the identifier / constant
     * @return the position if the identifier / constant exists, else return null
     */
    public Integer get(Object key) {
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

    public SortedMap<Integer, String> getSortedNodes() {
        SortedMap<Integer, String> elements = new TreeMap<>();
        for (HashNode head : array) {
            if (head == null)
                continue;

            while (head != null) {
                elements.put(head.value, head.key.toString());
                head = head.next;
            }
        }

        return elements;
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
                this.put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    /**
     * Returns the hash code value which is the sum of ascii characters % capacity
     */
    private Integer hashCode(Object key) {
        // char sum % capacity
        long sum = 0;
        String string = key.toString();
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
