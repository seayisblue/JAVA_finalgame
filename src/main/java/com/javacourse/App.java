package com.javacourse;

import com.javacourse.config.Constants;
import com.javacourse.config.ResourceConfig;
import com.javacourse.game.SubmarineWarGame;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        SwingUtilities.invokeLater(()->{

            ResourceConfig.loadAllResources();

            JFrame frame = new JFrame(Constants.GAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            SubmarineWarGame game = new SubmarineWarGame();
            frame.add(game.getGamePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            game.start();
        });

    }


}
