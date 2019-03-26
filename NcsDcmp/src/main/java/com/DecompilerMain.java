package com;

import com.ncsdcmp.Decompiler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author sad
 */
public class DecompilerMain {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Started decompiling of ncs script ");
        new Decompiler().decompile(new FileInputStream(new File("d:/disassembledIdNcs.txt")));
    }
}
