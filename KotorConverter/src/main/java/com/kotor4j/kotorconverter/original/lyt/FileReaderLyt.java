package com.kotor4j.kotorconverter.original.lyt;

import com.kotor4j.kotorconverter.BaseReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dmitry
 */
public class FileReaderLyt extends BaseReader {

    public LytFile loadFile(InputStream stream, String name) throws IOException {
        LytFile lyt = new LytFile();
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = StringUtils.stripStart(line, " \t");
            if (line.startsWith("#")) {
                continue;
            }else if (line.toLowerCase().startsWith(name.toLowerCase())) {//some strange cases like M12aa_01m at end of file
                continue;
            }else if (line.startsWith("filedependancy")) {
                lyt.setFileDependency(line.substring("filedependancy".length()).trim());
            } else if (line.startsWith("beginlayout") || line.startsWith("donelayout")) {
                continue;
            } else if (line.startsWith("roomcount")) {
                int roomsCount = Integer.parseInt(line.substring("roomcount".length()).trim());
                for (int i = 0; i < roomsCount; i++) {
                    String roomLine = scanner.nextLine();
                    roomLine = StringUtils.stripStart(roomLine, " \t");
                    Map<String, Object> resultMap = new HashMap<>();
                    String[] splitted = roomLine.split(" ");
                    resultMap.put("model", splitted[0]);
                    resultMap.put("x", Float.parseFloat(splitted[1]));
                    resultMap.put("y", Float.parseFloat(splitted[2]));
                    resultMap.put("z", Float.parseFloat(splitted[3]));
                    lyt.getRooms().add(resultMap);
                }
            } else if (line.startsWith("trackcount")) {
                int objCount = Integer.parseInt(line.substring("trackcount".length()).trim());
                for (int i = 0; i < objCount; i++) {
                    String objLine = scanner.nextLine();
                    objLine = StringUtils.stripStart(objLine, " \t");
                    Map<String, Object> resultMap = new HashMap<>();
                    String[] splitted = objLine.split(" ");
                    resultMap.put("model", splitted[0]);
                    resultMap.put("x", Float.parseFloat(splitted[1]));
                    resultMap.put("y", Float.parseFloat(splitted[2]));
                    resultMap.put("z", Float.parseFloat(splitted[3]));
                    lyt.getTrackObjects().add(resultMap);
                }
            } else if (line.startsWith("obstaclecount")) {
                int objCount = Integer.parseInt(line.substring("obstaclecount".length()).trim());
                for (int i = 0; i < objCount; i++) {
                    String objLine = scanner.nextLine();
                    objLine = StringUtils.stripStart(objLine, " \t");
                    Map<String, Object> resultMap = new HashMap<>();
                    String[] splitted = objLine.split(" ");
                    resultMap.put("model", splitted[0]);
                    resultMap.put("x", Float.parseFloat(splitted[1]));
                    resultMap.put("y", Float.parseFloat(splitted[2]));
                    resultMap.put("z", Float.parseFloat(splitted[3]));
                    lyt.getObstacleObjects().add(resultMap);
                }
            } else if (line.startsWith("doorhookcount")) {
                int objCount = Integer.parseInt(line.substring("doorhookcount".length()).trim());
                for (int i = 0; i < objCount; i++) {
                    String objLine = scanner.nextLine();
                    objLine = StringUtils.stripStart(objLine, " \t");
                    Map<String, Object> resultMap = new HashMap<>();
                    String[] splitted = objLine.split(" ");
                    resultMap.put("room", splitted[0]);
                    resultMap.put("door", splitted[1]);
                    resultMap.put("unknown", splitted[2]);
                    resultMap.put("x", Float.parseFloat(splitted[3]));
                    resultMap.put("y", Float.parseFloat(splitted[4]));
                    resultMap.put("z", Float.parseFloat(splitted[5]));
                    resultMap.put("rw", Float.parseFloat(splitted[6]));
                    resultMap.put("rx", Float.parseFloat(splitted[7]));
                    resultMap.put("ry", Float.parseFloat(splitted[8]));
                    resultMap.put("rz", Float.parseFloat(splitted[9]));
                    lyt.getDoorHooks().add(resultMap);
                }
            } else {
                throw new IllegalStateException("Lyt parser cannot parse line [" + line + "] in file ["+name+"]");
            }
        }
        return lyt;
    }
}
