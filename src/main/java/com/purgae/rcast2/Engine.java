package com.purgae.rcast2;

import java.util.HashSet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class Engine extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 2L;
    private boolean isRendering = false;
    private float stateTime = 0f;
    private float updateInterval = 1000/60f;
    private final Timer timer;
    protected final HashSet<Character> keys = new HashSet<Character>();

    public Engine() {
        timer = new Timer(1000/60, this);
        timer.setRepeats(true);
        this.setVisible(true);
        timer.start();
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000/60L);
                    if(isRendering)
                        this.paintImmediately(0, 0, getWidth(), getHeight());
                }
                catch(Exception e){

                } 
                
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent event){
        if(isRendering){
            stateTime += 60f;
            if(stateTime >= updateInterval){
                this.update();
                stateTime = 0;
            }
        }
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);
    protected abstract void init();

    public void start() {
        init();
        isRendering = true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Clear to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        //Reset the color for the inherited class
        g.setColor(Color.BLACK);

        draw((Graphics2D) g);
    }

    @Override
    public void update(Graphics g){
        super.update(g);
        //Clear to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        //Reset the color for the inherited class
        g.setColor(Color.BLACK);

        draw((Graphics2D) g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyChar());
	}
}