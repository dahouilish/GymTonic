package epf.projectgymtonic.services;

import javax.swing.*;

public class DisplayServices {

    public DisplayServices() {
        super();
    }

    public void displayAlertMessage(String title, String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
