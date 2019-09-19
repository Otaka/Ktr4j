package com.kotor4j.kotorconverter.original.vis;

import com.kotor4j.utils.StringBuilderWithPadding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class VisFile {
    private Map<String,List<String>>visData=new HashMap<>();

    public Map<String, List<String>> getVisData() {
        return visData;
    }

    public String toRawVisFile(){
        StringBuilderWithPadding sb=new StringBuilderWithPadding("  ");
        for(String key:visData.keySet()){
            List<String>connectedCells=visData.get(key);
            sb.println(key+" "+connectedCells.size());
            sb.incLevel();
            for(String connectedCell:connectedCells){
                sb.println(connectedCell);
            }

            sb.decLevel();
        }
        return sb.toString();
    }
}
