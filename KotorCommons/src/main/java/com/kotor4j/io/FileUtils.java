package com.kotor4j.io;

import java.io.File;
import org.apache.tools.ant.DirectoryScanner;

/**
 * @author Dmitry
 */
public class FileUtils {

    public static File[] searchFilesByGlobPath(String path, boolean caseSensitive) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{path});
        scanner.setErrorOnMissingDir(false);
        scanner.setFollowSymlinks(false);
        scanner.setCaseSensitive(caseSensitive);
        scanner.scan();
        String[] filesPaths = scanner.getIncludedFiles();
        File[] files = new File[filesPaths.length];
        for (int i = 0; i < files.length; i++) {
            String filePath = filesPaths[i];
            files[i] = new File(filePath);
        }
        return files;
    }

    public static String wildcardToRegex(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                case '^': // escape character in cmd.exe
                    s.append("\\");
                    break;
                // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return (s.toString());
    }
}
