package com.kotor4j.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;

/**
 * @author sad
 */
public class NwnByteArrayInputStream extends InputStream {

    private final StringBuilder tempBuffer = new StringBuilder();
    private final byte[] buffer;
    private int pos = 0;

    public NwnByteArrayInputStream(File file) {
        try {
            this.buffer = FileUtils.readFileToByteArray(file);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading file [" + file.getAbsolutePath() + "]", ex);
        }
    }
    
    public int length(){
        return buffer.length;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public NwnByteArrayInputStream(byte[] buf) {
        this.buffer = buf;
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    public int getPosition() {
        return pos;
    }

    public String readStringEndedWith(char... endingChars) throws IOException {
        while (true) {
            int value = read();
            if (value == -1) {//end of stream
                return null;
            }
            for (int i = 0; i < endingChars.length; i++) {
                if (endingChars[i] == value) {
                    String result = tempBuffer.toString();
                    tempBuffer.setLength(0);
                    return result;
                }
            }

            tempBuffer.append((char) value);
        }
    }

    public String readTabEndedString() throws IOException {
        while (true) {
            int value = read();
            if (value == 0) {
                return null;
            }
            if (value == '\t') {
                String result = tempBuffer.toString();
                tempBuffer.setLength(0);
                return result;
            }

            tempBuffer.append((char) value);
        }
    }

    public String readNullTerminatedString() throws IOException {
        while (true) {
            int value = read();
            if (value == -1) {
                tempBuffer.setLength(0);
                return null;
            }
            if (value == 0) {
                String result = tempBuffer.toString();
                tempBuffer.setLength(0);
                return result;
            }

            tempBuffer.append((char) value);
        }
    }

    public String readString(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            int value = read();
            if (value == 0) {

            } else {
                tempBuffer.append((char) value);
            }

        }
        String result = tempBuffer.toString();
        tempBuffer.setLength(0);
        return result;
    }

    public int readInt() throws IOException {
        return (read() & 0xFF) | ((read() & 0xFF) << 8) | ((read() & 0xFF) << 16) | ((read() & 0xFF) << 24);
    }

    public int readByte() throws IOException {
        return read() & 0xFF;
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void skip(int count) throws IOException {
        super.skip(count);
    }

    public long readLong() throws IOException {
        return (read() & 0xFF) | (((long) read() & 0xFF) << 8) | ((read() & 0xFF) << 16) | ((read() & 0xFF) << 24)
                | (((long) read() & 0xFF) << 32) | (((long) read() & 0xFF) << 40) | (((long) read() & 0xFF) << 48) | (((long) read() & 0xFF) << 56);
    }

    public short readShort() throws IOException {
        return (short) ((read() & 0xFF) | ((read() & 0xFF) << 8));
    }

    @Override
    public int read() throws IOException {
        if (pos >= buffer.length) {
            return -1;
        }
        int val = buffer[pos] & 0xFF;
        pos++;
        return val;
    }
}
