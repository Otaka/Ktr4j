package com.kotor4j.kotorconverter.serializer;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class KSADumper {

    private DataOutputStream dataOutputStream;

    public KSADumper(File ksaFile) {
        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ksaFile), 5_000_000));
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot open file [" + ksaFile.getAbsolutePath() + "]", ex);
        }
    }

    public void writeObject(Object object) throws IOException {
        Class clazz = object.getClass();
        RecordTag recordTagAnnotation = (RecordTag) clazz.getAnnotation(RecordTag.class);
        if (recordTagAnnotation == null) {
            throw new IllegalArgumentException("You want to write record of class [" + clazz.getName() + "] but it is not marked with RecordTag annotation");
        }

        dataOutputStream.writeByte(recordTagAnnotation.value());
        dataOutputStream.writeByte(1);//define new record
        while (clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                FieldTag fieldAnnotation = f.getAnnotation(FieldTag.class);
                if (fieldAnnotation != null) {
                    Class fType = f.getType();
                    Object value = getFieldValue(f, object);
                    if (value == null) {
                        continue;
                    }
                    if (fType == int.class) {
                        dataOutputStream.writeInt((int) value);
                    } else if (fType == String.class) {
                        dataOutputStream.writeUTF((String) value);
                    } else if (fType == float.class) {
                        dataOutputStream.writeFloat((float) value);
                    } else if (fType == boolean.class) {
                        dataOutputStream.writeBoolean((boolean) value);
                    } else if (fType == long.class) {
                        dataOutputStream.writeLong((long) value);
                    } else if (fType == int[].class) {
                        int[] arr = (int[]) value;
                        dataOutputStream.writeInt(arr.length);
                        for (int i = 0; i < arr.length; i++) {
                            int iValue = arr[i];
                            dataOutputStream.writeInt(iValue);
                        }
                    } else if (fType == String[].class) {
                        String[] arr = (String[]) value;
                        dataOutputStream.writeInt(arr.length);
                        for (int i = 0; i < arr.length; i++) {
                            String sValue = arr[i];
                            dataOutputStream.writeUTF(sValue);
                        }
                    } else if (fType == float[].class) {
                        float[] arr = (float[]) value;
                        dataOutputStream.writeInt(arr.length);
                        for (int i = 0; i < arr.length; i++) {
                            float iValue = arr[i];
                            dataOutputStream.writeFloat(iValue);
                        }
                    } else if (Enum.class.isAssignableFrom(fType)) {
                        int iValue = enumIndex(value);
                        dataOutputStream.writeInt(iValue);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private int enumIndex(Object enumObject) {
        try {
            return (int) enumObject.getClass().getMethod("getId", new Class[0]).invoke(enumObject, new Object[0]);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot get index from enum object [" + enumObject + "]", ex);
        }
    }

    private Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException("Error while get value from field [" + field.getName() + "]", ex);
        }
    }

    public void writeObject(List listOfObjects) throws IOException {
        for (Object object : listOfObjects) {
            writeObject(object);
        }
    }

    public void close() throws IOException {
        dataOutputStream.close();
    }
}
