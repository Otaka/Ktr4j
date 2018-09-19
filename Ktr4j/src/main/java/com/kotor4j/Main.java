package com.kotor4j;

import com.kotor4j.resourcemanager.Context;
import com.kotor4j.resourcemanager.ResourceManager;
import com.kotor4j.resourcemanager.ResourceRef;
import java.io.IOException;

/**
 * @author Dmitry
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Start Kotor4j");
        new Main().start();
        
    }
    
    private void start() throws IOException{
        Context context=loadContext();
        context.getResourceManager().setCurrentModule("danm13");
    }
    
    private Context loadContext() throws IOException{
        Context context=ResourceManager.loadContext(false);
        return context;
    }
}
