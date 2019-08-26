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
        this.setSize(1600, 900);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.add(currentEngine);
        addKeyListener(currentEngine);
        currentEngine.start(1600, 900);
        //pack();
        this.setVisible(true);
    }

    public static void main( String[] args )
    {
        new App();
    }
}
