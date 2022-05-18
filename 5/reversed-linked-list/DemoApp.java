package com.bobocode;

import java.util.Stack;

public class DemoApp {
    public static void main(String[] args) {
        var head = createLinkedList(4, 3, 9, 1);
        printReversedRecursively(head);
        printReversedUsingStack(head);
    }

    /**
     * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
     *
     * @param elements an array of elements that should be added to the list
     * @param <T>      elements type
     * @return head of the list
     */
    public static <T> Node<T> createLinkedList(T... elements) {
        if (elements == null || elements.length == 0) {
            return null;
        }
        var head = new Node<>(elements[0]);
        var currentNode = head;
        for (int i = 1; i < elements.length; i++) {
            currentNode.next = new Node<>(elements[i]);
            currentNode = currentNode.next;
        }
        return head;
    }

    /**
     * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
     * just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedRecursively(Node<T> head) {
        if(head.next == null){
            System.out.print(head.element);
        }
        while (head.next != null){
            printReversedRecursively(head.next);
            System.out.print(" -> " + head.element);
            return;
        }
    }

    /**
     * Prints a list in a reserved order using a {@link java.util.Stack} instance. Please note that it should not change
     * the list, just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedUsingStack(Node<T> head) {
        Stack<T> printStack = new Stack<>();
        printStack.push(head.element);

        var currentNode = head.next;
        while (currentNode != null){
            printStack.push(currentNode.element);
            currentNode = currentNode.next;
        }

        while (!printStack.isEmpty()) {
            System.out.print(printStack.pop());
            if (!printStack.isEmpty()){
                System.out.print(" -> ");
            }
        }
    }
}