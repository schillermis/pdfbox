/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.apache.fontbox.util;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mdo
 */
public class IntIntMapTest
{
  private IntIntMap defaultMap;

  @BeforeEach
  public void setUp()
  {
    defaultMap = new IntIntMap(3);
    defaultMap.put(5, 8);
    defaultMap.put(7, 11);
    defaultMap.put(3, 17);
  }

  @Test
  public void testSize()
  {
    IntIntMap sizeMap = new IntIntMap(2);
    assertEquals(0, sizeMap.size(), "Initial size must be 0");

    sizeMap.put(5, 10);
    sizeMap.put(2, 11);
    assertEquals(2, sizeMap.size(), "Size after 2 inserts");

    sizeMap.put(5, 7);
    assertEquals(2, sizeMap.size(), "Size after 2 inserts, 1 overwrite");

    //make sure the internal structure has to be resized
    sizeMap.put(11, 7);
    sizeMap.put(12, 7);
    sizeMap.put(13, 7);
    assertEquals(5, sizeMap.size(), "Size after 5 inserts, 1 overwrite");
  }

  @Test
  public void testPutAndGet()
  {
    IntIntMap putMap = new IntIntMap(8);
    assertEquals(IntIntMap.NO_VALUE, putMap.get(5), "Get of inexisting key must return Integer.MIN_VALUE");

    putMap.put(5, 10);
    assertEquals(10, putMap.get(5), "Get of value for key '5'");

    putMap.put(6, 12);
    putMap.put(2, 19);
    assertEquals(12, putMap.get(6), "Get of value for key '6'");

    putMap.put(6, 999);
    assertEquals(999, putMap.get(6), "Get of value for key '6'");
  }

  @Test
  public void entryIterator_returns_all_elements_in_order()
  {
    IntIntMap.EntryIterator iterator = defaultMap.entryIterator();

    iterator.next();
    assertEquals(3, iterator.getKey());
    assertEquals(17, iterator.getValue());
    iterator.next();
    assertEquals(5, iterator.getKey());
    assertEquals(8, iterator.getValue());
    iterator.next();
    assertEquals(7, iterator.getKey());
    assertEquals(11, iterator.getValue());
  }

  @Test
  public void entryIterator_hasNext_only_returns_false_on_last_entry()
  {
    IntIntMap.EntryIterator iterator = defaultMap.entryIterator();

    assertTrue(iterator.hasNext());
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertTrue(iterator.hasNext());
    iterator.next();
    assertFalse(iterator.hasNext());
  }

  @Test
  public void entryIterator_throws_if_next_is_called_on_last_entry()
  {
    IntIntMap.EntryIterator iterator = defaultMap.entryIterator();
    iterator.next();
    iterator.next();
    iterator.next();

    //throw
    assertThrows(NoSuchElementException.class, () -> {
      iterator.next();
    });
  }
  
  @Test
  public void empty_map_does_not_contain_any_key()
  {
    IntIntMap containsMap = new IntIntMap(8);
    assertFalse(containsMap.containsKey(5), "Key '5' must not exist");
  }
  
  @Test
  public void contains_returns_true_if_key_is_in_map()
  {
    assertTrue(defaultMap.containsKey(5), "Key '5' must exist");
  }
  
  @Test
  public void contains_returns_false_if_key_is_not_in_map()
  {
    assertFalse(defaultMap.containsKey(4), "Key '4' must not exist");
  }  
}
