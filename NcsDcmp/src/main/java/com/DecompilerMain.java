package com;

import com.ncsdcmp.Decompiler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author sad
 */
public class DecompilerMain {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Started decompiling of ncs script ");
        new Decompiler(new File("D:\\temp\\ktr\\scripts\\K1\\nwscript.nss")).decompile(new FileInputStream(new File("D:\\temp\\ktr\\scripts\\K1\\base_arithm.ncs")));
    }
}
