package com.purgae.rcast2;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App extends Frame
{
    private static final long serialVersionUID = 1L;

    private Engine currentEngine = new RayCasting();

    App() {
        super();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event){
                System.exit(0);
            }
        });
        this.setSize(800, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.add(currentEngine).setSize(this.getSize());
        addKeyListener(currentEngine);
        currentEngine.start();
        pack();
        this.setVisible(true);
    }

    public static void main( String[] args )
    {
        new App();
    }
}
