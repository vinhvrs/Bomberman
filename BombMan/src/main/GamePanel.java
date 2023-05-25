package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import bomb.Bomb;
import bomb.Explode;
import entity.Monster;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16; // 16x16 pixels
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 23; // set size the screen and map
    public final int maxScreenRow = 17; // set size the screen and map
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // World set up
    public final int maxWorldCol = 27;
    public final int maxWorldRow = 12;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();
    public Sound sfx = new Sound();
    public Thread gameThread;
    public CollisionCheck Colcheck = new CollisionCheck(this);

    public Player player = new Player(this, keyH);
    public Bomb bombs[] = new Bomb[10];
    public Explode[] explodes = new Explode[40];
    public SuperObject obj[] = new SuperObject[20];
    public Monster mons[] = new Monster[10];

    // seting object value
    public AssetSetter aSetter = new AssetSetter(this);
    public Random_item gacha = new Random_item(this);

    // Game state
    public int gameState = 0;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int Difficult = 3;
    public final int ENDGAME = 4;
    public final int optionState = 5;

    // Choosing difficult mode
    public int gameDifficult = 0;
    public final int easy = 1;
    public final int normal = 2;
    public final int hard = 3;

    // Player position default:
    int playerX = 1;
    int playerY = 1;
    int playerSpeed = 4;

    // UI
    public UI ui = new UI(this);

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // this function for rendering front/ back buffer at the same time
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void setupGame() {

        aSetter.setObject();
        aSetter.setMon();

        PlayMusic(7);
        if (gameState == playState) {
            // gameState = playState;
        } else
            gameState = titleState;

        // if (gameDifficult > 9) {
        System.out.println(gameDifficult);
        // }

        System.out.println(gameDifficult + " From Gamepanel");

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1e9 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;
            if (delta >= 1) {
                update(); // update position
                repaint(); // re-draw screen
                delta--;
            }

        }

    }

    public void update() {

        if (gameState == playState) {
            for (int i = 0; i < mons.length; ++i) {
                if (mons[i] != null)
                    mons[i].update();
            }
            player.update();
        } else if (gameState == pauseState) {

        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            // Map drawing
            tileM.draw(g2);

            // Object drawing
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            // Player drawing
            player.draw(g2);

            // Bombs drawing
            for (int i = 0; i < player.bomb_count; i++) {
                if (bombs[i] != null && bombs[i].worldX > 0 && bombs[i].worldY > 0) {
                    bombs[i].draw(g2, this);
                    // bombs[i].Duration(i);
                }
            }
            for (int i = 0; i < explodes.length; i++) {
                if (explodes[i] != null) {
                    explodes[i].draw(g2, this);
                }
            }

            // Monster drawing
            for (int i = 0; i < mons.length; i++) {
                if (mons[i] != null) {
                    mons[i].draw(g2, this);
                }
            }

            // UI drawing
            ui.draw(g2);
        }

        g2.dispose();

    }

    public void PlayMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void StopMusic() {
        sound.stop();
    }

    public void playSE(int i) {
        sfx.setFile(i);
        sfx.play();
    }

}