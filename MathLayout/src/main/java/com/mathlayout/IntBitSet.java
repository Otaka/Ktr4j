package com.mathlayout;

/**
 * @author Dmitry
 */
public class IntBitSet {

    private int buffer;

    public void clear() {
        buffer = 0;
    }

    public void setAll() {
        buffer = 0xFFFFFFFF;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public void set(int bit) {
        if (bit < 0 || bit >= 32) {
            throw new IllegalArgumentException("Bit index cannot be more that 31 and less than 0");
        }
        int value = 1 << bit;
        buffer = buffer | value;
    }

    public void clear(int bit) {
        if (bit < 0 || bit >= 32) {
            throw new IllegalArgumentException("Bit index cannot be more that 31 and less than 0");
        }
        int value = 1 << bit;

        buffer = buffer & (~value);
    }

    public boolean get(int bit) {
        if (bit < 0 || bit >= 32) {
            throw new IllegalArgumentException("Bit index cannot be more that 31 and less than 0");
        }
        int value = 1 << bit;
        return (buffer & value) != 0;
    }
}
