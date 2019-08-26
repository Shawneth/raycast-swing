package com.purgae.rcast2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class RayCasting extends Engine {

    private final int[][] map = new int[][] 
    {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
        {1, 1, 1, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    int TILEMAP_WIDTH = map[0].length;
    int TILEMAP_HEIGHT = map.length;

    private float angle = 0f;
    private float fov = 60f;
    private float px = 0;
    private float py = 0;
    private int tileWidth, tileHeight;

    private boolean autoRotate = true;

    public void init(int screenWidth, int screenHeight) {
        setSize(new Dimension(screenWidth, screenHeight));
        tileWidth = screenWidth/TILEMAP_WIDTH;
        tileHeight = screenHeight/TILEMAP_HEIGHT;
        px = 5 * tileWidth;
        py = 5 * tileHeight;
        System.out.println(getWidth() + "px wide.");
        System.out.println(getHeight() + "px high.");
        System.out.println("--");
        System.out.println(TILEMAP_WIDTH + " tiles wide.");
        System.out.println(TILEMAP_HEIGHT + " tiles high.");
    }

    public void update() {
        if(keys.contains('d') || autoRotate){
            angle -= 2f;
        }
        if(keys.contains('a')){
            angle += 2f;
        }
        if(keys.contains('w')){
            px += Math.toDegrees(Math.cos(Math.toRadians(angle))) / 10f;
            py += Math.toDegrees(Math.sin(-Math.toRadians(angle))) / 10f;
        }
        if(keys.contains('s')){
            px -= Math.toDegrees(Math.cos(Math.toRadians(angle))) / 10f;
            py -= Math.toDegrees(Math.sin(-Math.toRadians(angle))) / 10f; 
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Space hit?
        if(e.getKeyCode() == 32) {
            autoRotate = !autoRotate;
        }
    }

    /**
     * Implement stuff here
     */
    public void draw(Graphics2D g){
        //Draw map
        for(int i = 0; i < map.length; i++){
            for(int k = 0; k < map[i].length; k++){
                if(map[i][k] == 1){
                    g.setColor(Color.black);
                }
                else {
                    g.setColor(Color.WHITE);
                }
                //Tile area divided by four will center the map.
                g.fillRect(k * tileWidth, i * tileHeight, tileWidth, tileHeight);
                //g.setColor(Color.RED);
                //g.drawString("Y: " + (i * tileHeight - tileHeight/2), k * tileWidth - tileWidth/4, i * tileHeight - tileHeight/2);
            }
        }

        float rayStartAngle = angle - fov/2;
        for(int x = 0; x < fov; x++){
            /*
            * origin is x and y.
            * for each iteration, add the far-left fov angle to the maximum fov MINUS the current iteration step.
            * Convert the value to radians. Give it a magnitude of 4.
            * Pass the value through sin/cos and add the offset of the current position (i.e x = 10, so add 10 as well.)
            * Convert the final value to degrees and cast it to an int so it can be plotted on the canvas.
            */
            
            //Let's get that wall hit
            boolean isHit = false;
            float xCheck = px;
            float yCheck = py;
            for(int d = 0; d < 500; d++){
                g.setColor(Color.darkGray);
                g.fillRect((int)xCheck, (int)yCheck, 1, 1);
                xCheck += Math.toDegrees(Math.cos(Math.toRadians(rayStartAngle + fov-x)) * 0.05f);
                yCheck += Math.toDegrees(Math.sin(-Math.toRadians(rayStartAngle + fov-x)) * 0.05f);
                int xWhole = (int) (xCheck/tileWidth);
                int yWhole = (int) (yCheck/tileHeight);
                if(yWhole >= 0 && yWhole < map.length && xWhole >= 0 && xWhole < map[yWhole].length){
                    if(map[yWhole][xWhole] == 1){
                        //WE HIT!!
                        break;
                    }
                }
            }
        }

        g.setColor(Color.WHITE);
            g.drawString("Starting angle: " + rayStartAngle, 5, 10);
            g.drawString("x: " + px, 5, 30);
            g.drawString("y: " + py, 5, 40);

    }
}