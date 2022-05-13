package com.bobocode;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
@SuppressWarnings("unchecked")
public class HashTable<K, V> {

    private int capacity = 16;
    private Node<K, V>[] table = new Node[capacity];
    private int size;
    private final float THRESHOLD = 0.8F;
    private final float RESIZE_MULTIPLIER = 1.8F;

    public HashTable() {
    }

    public HashTable(Node<K, V> element) {
        put(element.key, element.value);
    }

    /**
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
     */
    public V put(K key, V value) {
        if(key == null){
            processNullKey();
            return null;
        }

        int hash = Math.abs(key.hashCode() % capacity);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> currentNode = table[hash];

            if (currentNode.key == newNode.key) {
                V oldValue = currentNode.value;
                currentNode.value = newNode.value;
                return oldValue;
            }

            while (currentNode.next != null) {
                currentNode = currentNode.next;

                if (currentNode.key == newNode.key) {
                    V oldValue = currentNode.value;
                    currentNode.value = newNode.value;
                    return oldValue;
                }

            }
            currentNode.next = newNode;
        }

        size++;

        if (size > capacity * THRESHOLD) {
            resize();
        }
        return null;
    }

    private void processNullKey() {
        System.out.println("WARNING. The KEY cannot be NULL!");
    }


    private void resize() {
        printResizeWarning();
        Node<K, V>[] oldTable = table;
        capacity *= RESIZE_MULTIPLIER;
        size = 0;
        table = new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                Node<K, V> currentNode = oldTable[i];
                put(currentNode.key, currentNode.value);
                oldTable[i] = currentNode.next;
            }
        }
    }


    private void printResizeWarning() {
        System.out.println("_____________________[WARNING!]___________________________");
        System.out.println("Current HashTable needs a resize(capacity = " + capacity + ", size = " + size + "):\n");
        printTable();
        System.out.println("\nProcessing resize.");
        System.out.println("__________________________________________________________\n\n\n\n");
    }

    /**
     * Prints a content of the underlying table (array) according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
     * ...
     */
    public void printTable() {
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + ":   ");
            if (table[i] != null) {
                Node<K, V> currentNode = table[i];
                System.out.print(currentNode.key + " : [" + currentNode.value + "]");
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    System.out.print("   ->   " + currentNode.key + " : [" + currentNode.value + "]");
                }
            }
            System.out.println();
        }
    }
}