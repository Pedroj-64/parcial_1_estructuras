package com.uniquindio.edu.strucst.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
public class SinglyLinkedListTest {

    private SimpleList<String> list;

    @Before
    public void setUp() {
    list = new SinglyLinkedList<>();
    }

    private void fill(String... values) {
    for (String value : values) {
        list.add(value);
    }
    }

    private List<String> contents() {
    List<String> out = new ArrayList<>();
    list.forEach(out::add);
    return out;
    }

    @Test
    public void empty_isEmptyAndSizeZero() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    public void empty_removeReturnsFalse() {
        assertFalse(list.remove("A"));
    }

    @Test
    public void empty_getAndRemoveAtFail() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(0));
    }

    @Test
    public void empty_iteratorHasNoElements() {
        Iterator<String> it = list.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void single_addAndGet() {
        list.add("A");
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals("A", list.get(0));
    }

    @Test
    public void single_removeOnlyElementLeavesListEmptyAndReusable() {
        list.add("A");
        assertTrue(list.remove("A"));
        assertTrue(list.isEmpty());
        list.add("B");
        assertEquals(List.of("B"), contents());
    }

    @Test
    public void removal_removeFirst() {
        fill("A", "B", "C", "D");
        assertTrue(list.remove("A"));
        assertEquals(List.of("B", "C", "D"), contents());
    }

    @Test
    public void removal_removeMiddle() {
        fill("A", "B", "C", "D");
        assertTrue(list.remove("C"));
        assertEquals(List.of("A", "B", "D"), contents());
    }

    @Test
    public void removal_removeLastKeepsTailConsistent() {
        fill("A", "B", "C", "D");
        assertTrue(list.remove("D"));
        list.add("E");
        assertEquals(List.of("A", "B", "C", "E"), contents());
    }

    @Test
    public void removal_removeNonExistent() {
        fill("A", "B", "C", "D");
        assertFalse(list.remove("Z"));
        assertEquals(4, list.size());
    }

    @Test
    public void removal_removeAtPositions() {
        fill("A", "B", "C", "D");
        assertEquals("A", list.removeAt(0));
        assertEquals("C", list.removeAt(1));
        assertEquals("D", list.removeAt(1));
        assertEquals(List.of("B"), contents());
        list.add("E");
        assertEquals("E", list.get(1));
    }

    @Test
    public void removal_removeAtInvalidIndex() {
        fill("A", "B", "C", "D");
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(4));
    }

    @Test
    public void keepsInsertionOrder() {
    fill("PQ105", "PQ942", "PQ731");
    assertEquals(List.of("PQ105", "PQ942", "PQ731"), contents());
    assertEquals("PQ942", list.get(1));
    assertEquals("[PQ105 -> PQ942 -> PQ731]", list.toString());
    }

    @Test
    public void worksWithOtherTypes() {
    SimpleList<Integer> numbers = new SinglyLinkedList<>();
    numbers.add(10);
    numbers.add(20);
    assertTrue(numbers.contains(20));
    assertEquals(30, numbers.get(0) + numbers.get(1));
    }

    @Test
    public void rejectsNull() {
    assertThrows(IllegalArgumentException.class, () -> list.add(null));
    }
}
