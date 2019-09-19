package com.kotor4j;

import java.io.IOException;

/**
 * @author Dmitry
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Start Kotor4j");
        new Main().start();
    }

    private void start() throws IOException {
        GameWindow gameWindow=new GameWindow();                
        gameWindow.start();
    }
}
