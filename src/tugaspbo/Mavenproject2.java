/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject2;

/**
 *
 * @author ekapr
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mavenproject2   extends JFrame {
    private JLabel timeDoneLabel;
    private JLabel timeLeftLabel;
    private PieChartPanel pieChartPanel;
    private Timer timer;
    private int totalTime = 2 * 60 * 60; // 2 hours in seconds
    private int elapsedTime = 0; // Initial elapsed time in seconds

    public Mavenproject2() {
        setTitle("Time Indicator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        timeDoneLabel = new JLabel("Done : 0 hr, 0 min, 43 sec.");
        timeLeftLabel = new JLabel("Left : 1 hr, 59 min, 17 sec.");
        infoPanel.add(timeDoneLabel);
        infoPanel.add(timeLeftLabel);

        pieChartPanel = new PieChartPanel();

        add(infoPanel, BorderLayout.NORTH);
        add(pieChartPanel, BorderLayout.CENTER);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                updateLabels();
                pieChartPanel.repaint();
                if (elapsedTime >= totalTime) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void updateLabels() {
        int remainingTime = totalTime - elapsedTime;
        int hoursDone = elapsedTime / 3600;
        int minutesDone = (elapsedTime % 3600) / 60;
        int secondsDone = elapsedTime % 60;

        int hoursLeft = remainingTime / 3600;
        int minutesLeft = (remainingTime % 3600) / 60;
        int secondsLeft = remainingTime % 60;

        timeDoneLabel.setText(String.format("Done : %d hr, %d min, %d sec.", hoursDone, minutesDone, secondsDone));
        timeLeftLabel.setText(String.format("Left : %d hr, %d min, %d sec.", hoursLeft, minutesLeft, secondsLeft));
    }

    class PieChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            int min = Math.min(width, height);
            int pieSize = min - 50;
            int x = (width - pieSize) / 2;
            int y = (height - pieSize) / 2;

            int doneAngle = (int) ((double) elapsedTime / totalTime * 360);
            int leftAngle = 360 - doneAngle;

            g2d.setColor(Color.RED);
            g2d.fillArc(x, y, pieSize, pieSize, 90, -doneAngle);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillArc(x, y, pieSize, pieSize, 90 - doneAngle, -leftAngle);

            g2d.setColor(Color.BLACK);
            g2d.drawString((elapsedTime * 100 / totalTime) + "%", width / 2 - 10, height / 2 + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mavenproject2().setVisible(true);
            }
        });
    }
}
