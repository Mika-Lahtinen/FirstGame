package com.mine.firstgame;

import com.mine.firstgame.graphics.Render;
import com.mine.firstgame.graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Display extends Canvas implements Runnable {

    public static final long serialVersionUID = 1L;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "FirstGame 0.01";

    private Thread thread;
    private Screen screen;
    private Game game;
    private BufferedImage image;
    private int[] pixels;
    private boolean running = false;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

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
        int frames = 0;
        double unprossesedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 40.0;
        int tickCount = 0;
        boolean ticked = false;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprossesedSeconds += passedTime / 1000000000.0;

            while (unprossesedSeconds > secondsPerTick) {
                tick();
                unprossesedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 40 == 0) {
                    System.out.println(frames + "fps");
                    previousTime += 1000;
                    frames = 0;
                }

            }
            if (ticked) {
                render();
                frames++;
            }
            render();
            frames++;
        }
    }

    private void tick() {
        game.tick();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        System.arraycopy(screen.pixels, 0, pixels, 0, WIDTH * HEIGHT);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH+5, HEIGHT+5, null);
        g.dispose();
        bs.show();
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
        kurtisFrame.setResizable(true);
        kurtisFrame.setVisible(true);

        System.out.println("Running......");

        game.start();
    }
}
