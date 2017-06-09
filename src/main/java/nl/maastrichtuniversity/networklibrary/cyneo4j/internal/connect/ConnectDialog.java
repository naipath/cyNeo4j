package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sdi20386 on 08/06/2017.
 */
public class ConnectDialog extends JDialog implements ActionListener {

    private static final String CANCEL_CMD = "cancel";
    private static final String OK_CMD = "ok";

    private void init() {

        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setActionCommand(OK_CMD);
        okButton.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand(OK_CMD);
        cancelButton.setEnabled(false);

        JLabel urlLabel = new JLabel("URL");
        JTextField url = new JTextField();

        JLabel usernameLabel = new JLabel("Username");
        JTextField username = new JTextField();

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField password = new JPasswordField();

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(urlLabel).addComponent(url));

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
