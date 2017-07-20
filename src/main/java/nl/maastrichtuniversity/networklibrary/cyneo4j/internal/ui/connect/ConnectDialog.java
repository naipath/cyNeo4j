package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.connect;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.ConnectionParameter;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Predicate;

import static java.awt.Color.BLACK;

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
        DialogMethods.center(this);
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
        url = new JTextField("localhost");
        url.setForeground(Color.GRAY);
        url.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (url.getText().equals("localhost")) {
                    url.setText("");
                    url.setForeground(BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (url.getText().isEmpty()) {
                    url.setForeground(Color.GRAY);
                    url.setText("localhost");
                }
            }
        });

        JLabel usernameLabel = new JLabel("Username");
        username = new JTextField("neo4j");
        username.setForeground(Color.GRAY);
        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (username.getText().equals("neo4j")) {
                    username.setText("");
                    username.setForeground(BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setForeground(Color.GRAY);
                    username.setText("neo4j");
                }
            }
        });

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
            JOptionPane.showMessageDialog(this, " Invalid connection parameters");
        }
    }

}
