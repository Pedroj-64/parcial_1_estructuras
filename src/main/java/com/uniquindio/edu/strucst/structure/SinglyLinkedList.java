package com.uniquindio.edu.strucst.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SinglyLinkedList<T> implements SimpleList<T> {

    private static final class Node<T> {
        private final T value;
        private Node<T> next;

        private Node(T value) {
            this.value=value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("No se admiten elementos nulos");
        }

        Node<T> node = new Node<>(element);
        if (isEmpty()) {
            head = node;
        } else {
            tail.next = node;
        }
        tail=node;
        size++;
    }

    public boolean remove(T element) {
        if (isEmpty() || element == null) {
            return false;
        }
        if (Objects.equals(head.value, element)) {
            unlinkHead();
            return true;
        }
        Node<T> previous = head;
        while (previous.next != null && !Objects.equals(previous.next.value, element)) {
            previous = previous.next;
        }
        if (previous.next == null) {
            return false;
        }
        unlinkAfter(previous);
        return true;
    }

    private T unlinkHead() {
        T value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    private T unlinkAfter(Node<T> previous) {
        Node<T> removed = previous.next;
        previous.next = removed.next;
        if (removed == tail) {
            tail = previous;
        }
        size--;
        return removed.value;
    }

    private Node<T> nodeAt(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Posición " + index + " fuera de rango para una lista de tamaño " + size);
        }
    }

    
    public T removeAt(int index) {
        checkIndex(index);
        if (index == 0) {
            return unlinkHead();
        }
        return unlinkAfter(nodeAt(index - 1));
    }


    public T get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }


    public boolean contains(T element) {
        for (T value : this) {
            if (Objects.equals(value, element)) {
                return true;
            }
        }
        return false;
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(" -> ");
            }
        }
        return builder.append(']').toString();
    }

    private final class LinkedListIterator implements Iterator<T> {
        private Node<T> current = head;

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la lista");
            }
            T value = current.value;
            current = current.next;
            return value;
        }
    }


        

   
    
}
