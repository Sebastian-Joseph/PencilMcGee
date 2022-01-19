package main;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {

    public static int windowScale;

    public static void main(String[] args) throws IOException {

        // Prompt User for window scale
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        while (input <= 0 || input >= 5) {
            System.out.println("Set a window size (1, 2, 3, or 4)");
            try {
                input = scanner.nextInt();
                if (input <= 0 || input >= 5) {
                    System.out.println("Not a valid number.");
                    System.out.println();
                }
            }
            catch (Exception e) {
                input = 0;
                System.out.println("Not a valid number.");
                scanner.next();
                System.out.println();
            }
        }

        windowScale = input;
        System.out.println();
        scanner.close();
        ////////////////////////////////

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pencil McGee");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();


        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }

}
