package com.algorithms.sortingvisualizer;

import com.algorithms.sortingvisualizer.view.MainWindow;

import javax.swing.*;

/**
 * Starting point of the UI.
 */
public class SortingVisualizer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow mw = new MainWindow();
                mw.startDisplay();
            }
        });
    }

}
