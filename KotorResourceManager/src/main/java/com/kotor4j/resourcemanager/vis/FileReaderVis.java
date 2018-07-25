package com.kotor4j.resourcemanager.vis;

import com.kotor4j.io.NwnByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dmitry
 */
public class FileReaderVis {

    public VisFile loadFile(NwnByteArrayInputStream stream, String fileName) throws IOException {
        VisFile result = new VisFile();
        Map<String, List<String>> resultMap = result.getVisData();
        Scanner scanner = new Scanner(stream);
        List<String>currentSection=null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.isEmpty()){
                continue;
            }
            //m38ab provided count of lines in section, but real lines count is different
            //that is why rewrote to use ident to parse structure
            if(!line.startsWith(" ")){
                currentSection=new ArrayList<>();
                if(line.contains("  ")){
                    line=line.replace("  ", " ");
                }
                String[] parts = StringUtils.splitPreserveAllTokens(line.trim(), " ");
                String cellRef = parts[0].trim();
                resultMap.put(cellRef.toLowerCase(), currentSection);
            }else{
                currentSection.add(line.trim().toLowerCase());
            }
        }

        return result;
    }
}
