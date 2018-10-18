package epf.projectgymtonic.services;

import javax.swing.*;

public class DisplayServices {

    public DisplayServices() {
        super();
    }

    /**
     * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
     */

    // function used to display information thanks to a pop up
    public void displayAlertMessage(String title, String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
