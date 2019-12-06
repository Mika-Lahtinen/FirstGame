package com.mine.firstgame;

import com.mine.firstgame.graphics.Render;
import javafx.stage.Screen;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Display extends Canvas implements Runnable {

    public static final long serialVersionUID = 1L;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "FirstGame 0.01";
    private Render render;

    public Display() {
        Render screen = new Render(WIDTH, HEIGHT);
    }

    private Thread thread;
    private boolean running = false;

    private void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        while (running) {
            tick();
            render();
        }
    }

    private void tick() {

    }

    private void render() {

    }

    public static void main(String[] args) {
        Display game = new Display();
        JFrame kurtisFrame = new JFrame();

        kurtisFrame.pack();
        kurtisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        kurtisFrame.setLocationRelativeTo(null);
        kurtisFrame.setTitle(TITLE);
        kurtisFrame.setSize(WIDTH, HEIGHT);
        kurtisFrame.add(game);
        kurtisFrame.setResizable(false);
        kurtisFrame.setVisible(true);

        System.out.println("Running......");

        game.start();
    }
}
