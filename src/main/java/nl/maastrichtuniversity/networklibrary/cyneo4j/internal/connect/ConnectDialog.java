package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Predicate;

class ConnectDialog extends JDialog {

    private static final String CANCEL_CMD = "cancel";
    private static final String OK_CMD = "ok";
    private JTextField username;
    private JTextField url;
    private JPasswordField password;
    private boolean ok = false;
    private final Predicate<ConnectionParameter> connectionCheck;

    ConnectDialog(JFrame jFrame, Predicate<ConnectionParameter> connectionCheck) {
        super(jFrame);
        this.connectionCheck = connectionCheck;
    }

    void showConnectDialog() {
        init();
        center();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(true);
        pack();
        setVisible(true);
    }

    private ConnectionParameter getParameters() {
        return new ConnectionParameter(url.getText(), username.getText(), password.getPassword());
    }

    boolean isOk() {
        return ok;
    }

    private void init() {

        JButton okButton = new JButton("OK");
        okButton.addActionListener(this::ok);
        okButton.setActionCommand(OK_CMD);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(ae -> dispose());
        cancelButton.setActionCommand(CANCEL_CMD);

        JLabel urlLabel = new JLabel("URL");
        url = new JTextField();

        JLabel usernameLabel = new JLabel("Username");
        username = new JTextField();

        JLabel passwordLabel = new JLabel("Password");
        password = new JPasswordField();

        GridLayout layout = new GridLayout(4, 2);

        this.setLayout(layout);

        add(urlLabel);
        add(url);

        add(usernameLabel);
        add(username);

        add(passwordLabel);
        add(password);

        add(cancelButton);
        add(okButton);

    }

    private void ok(ActionEvent ae) {
        if(connectionCheck.test(getParameters())) {
            ok = true;
            dispose();
        } else {
            JOptionPane.showInputDialog("Invalid connection parameters");
        }
    }

    private void center() {
        Point cyLocation = getParent().getLocation();
        int height = getParent().getHeight();
        int width = getParent().getWidth();

        this.setLocation(new Point(cyLocation.x + (width / 4), cyLocation.y + (height / 4)));
    }
}
