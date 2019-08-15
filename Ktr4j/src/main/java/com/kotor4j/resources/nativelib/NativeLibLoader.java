package com.kotor4j.resources.nativelib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author Dmitry
 */
public class NativeLibLoader {

    /**
     * resourcePath should be like this:<br/>
     * /com/mylib/{os}{bitness}/somelib.{extension} <br/>
     * Placeholders:<br/>
     * {os} - name of operation system(windows, linux, mac)
     * {bitness} - 32 or 64
     * {extension} - extension for dynamic library that is used for current platform
     */
    public void loadNativeLibraryFromResources(String resourcePath) {
        String bitness = System.getProperty("sun.arch.data.model");
        String architecture = getOsName();
        String extension=getExtensionForArchitecture(architecture);
        String replacedPath = resourcePath
                .replace("{os}", architecture)
                .replace("{bitness}", bitness)
                .replace("{extension}", extension);
        File libFile = unpackResourceFile(replacedPath);
        if (libFile == null || !libFile.exists()) {
            throw new IllegalArgumentException("Cannot unpack library [" + resourcePath + "] to the directory [" + getTempDirectory() + "]");
        }
        System.load(libFile.getAbsolutePath());
    }

    private String getExtensionForArchitecture(String architecture) {
        switch (architecture.toLowerCase()) {
            case "windows":
                return "dll";
            case "linux":
                return "so";
            case "mac":
                return "dylib";
            default:
                return "dll";
        }
    }

    private String getOsName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "windows";
        }
        if (osName.contains("linux")) {
            return "linux";
        }
        if (osName.contains("mac")) {
            return "mac";
        }
        throw new IllegalArgumentException("Not supported os");
    }

    private String getLibName(String resourcePath) {
        return FilenameUtils.getName(resourcePath);
    }

    private File unpackResourceFile(String resourcePath) {
        InputStream libFileStream = NativeLibLoader.class.getResourceAsStream(resourcePath);
        if (libFileStream == null) {
            throw new IllegalArgumentException("Cannot find library under the path [" + resourcePath + "]");
        }

        String libraryChecksum = checkSum(libFileStream);
        libFileStream = NativeLibLoader.class.getResourceAsStream(resourcePath);
        File tempDir = getTempDirectory();
        String libraryName = getLibName(resourcePath);

        File destinationFileName = new File(tempDir, libraryName);
        if (destinationFileName.exists()) {
            String existedFileChecksum = getFileChecksum(destinationFileName);
            if (libraryChecksum.equals(existedFileChecksum)) {
                return destinationFileName;
            }
        }
        try (OutputStream outputStream = new FileOutputStream(destinationFileName)) {
            IOUtils.copy(libFileStream, outputStream, 1024 * 16);
            return destinationFileName;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                libFileStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String getFileChecksum(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return checkSum(inputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private File getTempDirectory() {
        File file = new File(System.getProperty("java.io.tmpdir"), "nativelib");
        file.mkdirs();
        return file;
    }

    private static String checkSum(InputStream fis) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Using MessageDigest update() method to provide input
            byte[] buffer = new byte[8192];
            int numOfBytesRead;
            while ((numOfBytesRead = fis.read(buffer)) > 0) {
                md.update(buffer, 0, numOfBytesRead);
            }
            byte[] hash = md.digest();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
