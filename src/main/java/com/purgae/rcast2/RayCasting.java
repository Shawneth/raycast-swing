package com.purgae.rcast2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class RayCasting extends Engine {

    private final int[][] map = new int[][] 
    {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private float angle = 0f;
    private float fov = 60f;
    private float px = 200f;
    private float py = 200f;
    private int widthPerRay;

    public RayCasting() {
        super();
    }

    public void init() {
        System.out.println("Our renderer is " + getWidth() + "px wide.");
        widthPerRay = this.getWidth()/(int)this.fov;
        setPreferredSize(new Dimension(widthPerRay * (int) this.fov, getHeight()));
        System.out.println("Our renderer is " + getWidth() + "px wide.");
    }

    public void update() {
        if(keys.contains('d')){
            angle -= 4f;
        }
        if(keys.contains('a')){
            angle += 4f;
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
    /**
     * Implement stuff here
     */
    public void draw(Graphics2D g){
        g.setColor(Color.gray);
        float rayStartAngle = angle - fov/2;
        float rayEndAngle = angle + fov/2;
        for(int x = 0; x < fov; x++){
            /*
            * origin is x and y.
            * for each iteration, add the far-left fov angle to the maximum fov MINUS the current iteration step.
            * Convert the value to radians. Give it a magnitude of 4.
            * Pass the value through sin/cos and add the offset of the current position (i.e x = 10, so add 10 as well.)
            * Convert the final value to degrees and cast it to an int so it can be plotted on the canvas.
            */
            g.drawLine((int)px, (int)py, 
                (int) (Math.toDegrees(Math.cos(Math.toRadians(rayStartAngle + fov-x)) * 4f) + px), 
                (int) (Math.toDegrees(Math.sin(-Math.toRadians(rayStartAngle + fov-x)) * 4f) + py));
        }
        g.setColor(Color.orange);
        //g.drawLine((int) px, (int) py, (int) Math.toDegrees(Math.cos(Math.toRadians(angle))) * (int) px, (int) Math.toDegrees(Math.sin(-Math.toRadians(angle))) * (int) py);
        
        g.setColor(Color.RED);
            g.drawString("Starting angle: " + rayStartAngle, 5, 10);
            g.drawString("Ending angle: " + rayEndAngle, 5, 20);
            g.drawString("x: " + px, 5, 30);
            g.drawString("y: " + py, 5, 40);

    }
}