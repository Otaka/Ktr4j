package com.mathlayout;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dmitry
 */
public class IntBitSetTest {

    @Test
    public void testIntBitSet() {
        IntBitSet bitSet = new IntBitSet();
        bitSet.set(0);
        assertEquals(0b000001, bitSet.getBuffer());
        bitSet.set(5);
        assertEquals(0b100001, bitSet.getBuffer());
        bitSet.set(3);
        assertEquals(0b101001, bitSet.getBuffer());
        bitSet.clear(3);
        assertEquals(0b100001, bitSet.getBuffer());
        bitSet.clear(5);
        assertEquals(0b000001, bitSet.getBuffer());
        bitSet.clear(0);
        assertEquals(0b000000, bitSet.getBuffer());

        bitSet.set(31);
        assertEquals(0x80000000, bitSet.getBuffer());
    }
    
    @Test
    public void testIntBitCheck() {
        IntBitSet bitSet = new IntBitSet();
        bitSet.setBuffer(0b101000);
        assertFalse(bitSet.get(0));
        assertFalse(bitSet.get(1));
        assertFalse(bitSet.get(2));
        assertTrue(bitSet.get(3));
        assertFalse(bitSet.get(4));
        assertTrue(bitSet.get(5));
    }
}
