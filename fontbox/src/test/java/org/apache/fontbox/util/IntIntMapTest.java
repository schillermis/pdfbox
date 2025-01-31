/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.apache.fontbox.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mdo
 */
public class IntIntMapTest
{
  @Test
  public void testSize()
  {
    IntIntMap sizeMap = new IntIntMap(2);
    Assert.assertEquals("Initial size must be 0", 0, sizeMap.size());

    sizeMap.put(5, 10);
    sizeMap.put(2, 11);
    Assert.assertEquals("Size after 2 inserts",2, sizeMap.size());

    sizeMap.put(5, 7);
    Assert.assertEquals("Size after 2 inserts, 1 overwrite", 2, sizeMap.size());

    //make sure the internal structure has to be resized
    sizeMap.put(11, 7);
    sizeMap.put(12, 7);
    sizeMap.put(13, 7);
    Assert.assertEquals("Size after 5 inserts, 1 overwrite", 5, sizeMap.size());
  }

  @Test
  public void testPutAndGet()
  {
    IntIntMap putMap = new IntIntMap(8);
    Assert.assertEquals("Get of inexisting key must return Integer.MIN_VALUE", IntIntMap.NO_VALUE, putMap.get(5));

    putMap.put(5, 10);
    Assert.assertEquals("Get of value for key '5'", 10, putMap.get(5));

    putMap.put(6, 12);
    putMap.put(2, 19);
    Assert.assertEquals("Get of value for key '6'", 12, putMap.get(6));

    putMap.put(6, 999);
    Assert.assertEquals("Get of value for key '6'", 999, putMap.get(6));
  }
}
