package Coinzy;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure the GUI creation runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create the main application window (JFrame)
            JFrame frame = new JFrame("Hello World Swing Application");
            
            // Set the default close operation to exit the application
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Create a label to display the "Hello World!" text
            JLabel label = new JLabel("Hello World!", JLabel.CENTER);
            
            // Add the label to the frame's content pane
            frame.getContentPane().add(label);
            
            // Set the size of the frame
            frame.setSize(300, 200);
            
            // Center the frame on the screen
            frame.setLocationRelativeTo(null);
            
            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
