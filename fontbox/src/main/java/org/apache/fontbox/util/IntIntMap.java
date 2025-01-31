package org.apache.fontbox.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 *
 * @author mdo
 */
public final class IntIntMap
{
  public static final int NO_VALUE = Integer.MIN_VALUE;

  private int keys[];
  private int values[];
  private int size;

  /**
   * This counter is incremented with every change done to this map. It is used
   * in the value and key iterators. These iterators will fail if the map is
   * changed during iteration.
   */
  private int changeCounter;

  /**
   * Default constructor. Creates an empty map whose internal data structures
   * are allocated to hold the given amount of mappings. If the number of
   * mappings exceeds this number, the map is growing dynamically.
   *
   * @param initialSize The number of mappings to initialize this map for
   */
  public IntIntMap(int initialSize)
  {
    keys = new int[initialSize];
    values = new int[initialSize];
  }

  /**
   * Add a new mapping to this map.
   *
   * @param key The mapping key
   * @param value The value to map to the key
   */
  public void put(int key, int value)
  {
    //keys of put calls are usually sequential. Handle this case with shortcut.
    int index = (size == 0 || keys[size-1] < key) ? -(size+1) : Arrays.binarySearch(keys, 0, size, key);

    //new key, insert
    if (index < 0) {
      index = (-index) - 1; //binary search returns negativ insert index
      ensureAllocatedSize(size + 1);

      //insert
      if (index < size) {
        System.arraycopy(keys, index, keys, index + 1, (size - index));
        System.arraycopy(values, index, values, index + 1, (size - index));
      }

      //only need to set the key if the index didn't exist before
      keys[index] = key;
      size++;
    }

    values[index] = value;
  }

  /**
   * Make sure that the internal data structures are big enough to hold the
   * given amount of mappings
   *
   * @param minimumSize amount of mappings
   */
  private void ensureAllocatedSize(int minimumSize)
  {
    if (keys.length < minimumSize) {
      int newSize = Math.max(minimumSize, (int) (keys.length * 1.2));

      keys = Arrays.copyOf(keys, newSize);
      values = Arrays.copyOf(values, newSize);
    }
  }

  /**
   * Returns the value associated to the given key. If the key is not in the map
   * this method will return Integer.MIN_VALUE.
   *
   * @param key The key to lookup
   * @return The associated value or Integer.MIN_VALUE if not found
   */
  public int get(int key)
  {
    int index = Arrays.binarySearch(keys, 0, size, key);
    return (index >= 0 ? values[index] : NO_VALUE);
  }

  /**
   * Returns the number of entries in this map
   *
   * @return Number of entries
   */
  public int size()
  {
    return size;
  }

  /**
   * Check if this map does not contain any entries.
   *
   * @return true if this map is empty
   */
  public boolean isEmpty()
  {
    return (size == 0);
  }

  /**
   * Create an iterator object to iterate over all keys in this map. The
   * returned iterator will interact directly with the data in this object. It
   * is therefore not allowed to change the map while the returned iterator is
   * in use. Doing so will cause the iterator to throw
   * {@code ConcurrentModificationException}s.
   *
   * Note: The iterator will iterate over the keys in an ordered fashion from
   * smallest to biggest key.
   *
   * @return An iterator to iterate over the keys in the map.
   */
  public KeyIterator keyIterator()
  {
    return new KeyIterator(changeCounter);
  }

  /**
   * Base class for the key and value iterator. Stores the current change
   * counter of the IntMap and provides a method to check if the counter
   * changed. Also implements the hasNext() method.
   */
  public final class KeyIterator
  {
    /**
     * A copy of the change counter at the time of the iterator creation. If the
     * underlying map changes while we iterate, the next/hasNext methods will
     * fail.
     */
    private final int changeCheckpoint;

    /**
     * Holds the index to the next element returned by the iterator
     */
    private int currentIdx;

    KeyIterator(int changeCounter)
    {
      this.changeCheckpoint = changeCounter;
      this.currentIdx = 0;
    }

    /**
     * Check if there are more elements to iterate
     *
     * @return true if there are more elements
     */
    public boolean hasNext()
    {
      checkIfModified();
      return currentIdx < size;
    }

    /**
     * @return the next element
     */
    public int next()
    {
      checkIfModified();
      checkIfIndexIsValid();

      return keys[currentIdx++];
    }

    void checkIfModified()
    {
      if (changeCheckpoint != changeCounter) {
        throw new ConcurrentModificationException("IntMap was changed whlie iterating"); // NOI18N
      }
    }

    void checkIfIndexIsValid()
    {
      if (currentIdx >= size) {
        throw new NoSuchElementException("Iterator does not contain any more elements"); // NOI18N
      }
    }
  }
}
