package com.purgae.rcast2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class RayCasting extends Engine {

    private final int[][] map = new int[][] 
    {
        {1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
        {1, 1, 1, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    final int darkestValue = 12, brightestValue = 200;
    final Color darkestColor = new Color(darkestValue, darkestValue, darkestValue), brightestColor = new Color(brightestValue, brightestValue, brightestValue);

    int TILEMAP_WIDTH = map[0].length;
    int TILEMAP_HEIGHT = map.length;

    private int screenWidth, screenHeight;
    private int tileWidth, tileHeight;

    private float rayDistance = 0.02f;
    private float rayRenderWidth;
    private float angle = 0f;
    private float fov = 50f;
    private float px = 0f;
    private float py = 0f;

    private boolean autoRotate = false;
    private boolean isDrawingMap = false;

    private int raysPerDegree = 4;
    private int rayCollideChecks = 800;

    public void init(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setSize(new Dimension(screenWidth, screenHeight));
        tileWidth = Math.round((float)screenWidth/(float)TILEMAP_WIDTH);
        tileHeight = Math.round((float)screenHeight/(float)TILEMAP_HEIGHT);
        px = 4 * tileWidth;
        py = 5 * tileHeight;
        rayRenderWidth = screenWidth/fov/raysPerDegree;
        System.out.println(getWidth() + "px wide.");
        System.out.println(getHeight() + "px high.");
        System.out.println("--");
        System.out.println(TILEMAP_WIDTH + " tiles wide.");
        System.out.println(TILEMAP_HEIGHT + " tiles high.");
    }

    public void update() {
        if(keys.contains('d') || autoRotate){
            angle -= 1f;
        }
        if(keys.contains('a')){
            angle += 1f;
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
        super.keyPressed(e);
        //Space hit?
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            autoRotate = !autoRotate;
        }
        if(e.getKeyChar() == 'm'){
            isDrawingMap = !isDrawingMap;
        }
    }

    /**
     * Implement stuff here
     */
    public void draw(Graphics2D g){

        if(isDrawingMap){
            drawMap(g);
        }
        else {
            drawEngine(g);
        }

        g.setColor(Color.WHITE);
            g.drawString("x: " + px, 5, 30);
            g.drawString("y: " + py, 5, 40);

    }

    private void drawEngine(Graphics2D g){

        //Render ceiling/floor
        GradientPaint gp = new GradientPaint(screenWidth/2, 0, brightestColor, screenWidth/2, screenHeight/2, darkestColor);
        g.setPaint(gp);
        g.fill(new Rectangle(screenWidth, screenHeight/2));
        gp = new GradientPaint(screenWidth/2, screenHeight/2, darkestColor, screenWidth/2, screenHeight, brightestColor);
        g.setPaint(gp);
        g.fill(new Rectangle(0, screenHeight/2, screenWidth, screenHeight/2));

        //Render Walls
        float rayStartAngle = angle - fov/2;
        for(float x = 0; x < fov * raysPerDegree; x++){
            boolean isHit = false;
            float xCheck = px;
            float yCheck = py;
            for(int d = 0; d < rayCollideChecks; d++){
                xCheck += Math.toDegrees(Math.cos(Math.toRadians(rayStartAngle + fov - (x/raysPerDegree))) * rayDistance);
                yCheck += Math.toDegrees(Math.sin(-Math.toRadians(rayStartAngle + fov - (x/raysPerDegree))) * rayDistance);
                int xWhole = Math.round(xCheck/tileWidth);
                int yWhole = Math.round(yCheck/tileHeight);
                if(yWhole >= 0 && yWhole < map.length && xWhole >= 0 && xWhole < map[yWhole].length){
                    if(map[yWhole][xWhole] == 1){
                        isHit = true;
                        break;
                    }
                }
            }
            if(isHit){
                float distance = (float) Math.sqrt(
                    Math.pow((yCheck - py), 2) + Math.pow((xCheck - px), 2)
                );
                float lineHeight = screenHeight/(distance/screenHeight)/12f;
                int shadeValue = Math.round(Math.min(brightestValue, Math.max(lineHeight/3.5f + 16f, darkestValue)));
                g.setColor(new Color(shadeValue, shadeValue, shadeValue));
                g.fillRect(
                    Math.round(x * rayRenderWidth),
                    Math.round((float)screenHeight/2 - (float)lineHeight/2),
                    (int)Math.ceil(rayRenderWidth),
                    Math.round(lineHeight)
                );
            }
        }
    }

    private void drawMap(Graphics2D g){
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
            }
        }

        float rayStartAngle = angle - fov/2;
        for(float x = 0; x < fov * raysPerDegree; x++){
            /*
            * origin is x and y.
            * for each iteration, add the far-left fov angle to the maximum fov MINUS the current iteration step.
            * Convert the value to radians. Give it a magnitude of 4.
            * Pass the value through sin/cos and add the offset of the current position (i.e x = 10, so add 10 as well.)
            * Convert the final value to degrees and cast it to an int so it can be plotted on the canvas.
            */
            
            //Let's get that wall hit
            float xCheck = px;
            float yCheck = py;
            for(int d = 0; d < rayCollideChecks; d++){
                g.setColor(Color.darkGray);
                xCheck += Math.toDegrees(Math.cos(Math.toRadians(rayStartAngle + fov-(x/raysPerDegree))) * rayDistance);
                yCheck += Math.toDegrees(Math.sin(-Math.toRadians(rayStartAngle + fov-(x/raysPerDegree))) * rayDistance);
                int xWhole = Math.round(xCheck/tileWidth);
                int yWhole = Math.round(yCheck/tileHeight);
                if(yWhole >= 0 && yWhole < map.length && xWhole >= 0 && xWhole < map[yWhole].length){
                    if(map[yWhole][xWhole] == 1){
                        g.drawLine((int)px + tileWidth/2, (int)py + tileHeight/2, (int)xCheck + tileWidth/2, (int)yCheck + tileHeight/2);
                        break;
                    }
                }
            }
        }
    }
}