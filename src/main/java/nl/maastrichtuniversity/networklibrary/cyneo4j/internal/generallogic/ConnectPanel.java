package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.generallogic;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.Neo4jServer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener, DocumentListener {

    private static final String CANCEL_CMD = "cancel";

    private static final String OK_CMD = "ok";

    private JDialog dialog = null;
    private Neo4jServer interactor = null;
    private JTextField servURL = null;
    private JLabel status = null;
    private JButton okButton = null;

    private ImageIcon green = null;
    private ImageIcon red = null;

    public ConnectPanel(JDialog dialog, Neo4jServer neo4jInteractor) {
        this.dialog = dialog;
        this.interactor = neo4jInteractor;

        green = new ImageIcon(getClass().getResource("/images/tick30.png"));
        red = new ImageIcon(getClass().getResource("/images/cross30.png"));

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel servURLLabel = new JLabel("Server URL");
        servURL = new JTextField();
        servURL.getDocument().addDocumentListener(this);

        status = new JLabel();
        status.setIcon(red);

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setActionCommand(OK_CMD);
        okButton.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand(CANCEL_CMD);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent(servURLLabel)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(servURL)
                    .addComponent(status))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(okButton)
                    .addComponent(cancelButton))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(servURLLabel)
                .addGroup(layout.createParallelGroup()
                    .addComponent(servURL)
                    .addComponent(status)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                )
        );

        this.setLayout(layout);

        if (interactor.getInstanceLocation() != null) {
            servURL.setText(interactor.getInstanceLocation());
        } else {
            servURL.setText("http://localhost:7474");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(CANCEL_CMD)) {
            closeUp();
        }

        if (e.getActionCommand().equals(OK_CMD)) {
            if (validURL()) {
                interactor.connect(getUrl());
            }
            closeUp();
        }
    }

    private boolean validURL() {
        return interactor.validateConnection(getUrl());
    }

    private String getUrl() {
        return servURL.getText();
    }

    protected JDialog getDialog() {
        return dialog;
    }

    protected void closeUp() {
        getDialog().setVisible(false);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkURLChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkURLChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkURLChange();
    }

    protected void checkURLChange() {
        if (validURL()) {
            status.setIcon(green);
            okButton.setEnabled(true);
        } else {
            status.setIcon(red);
            okButton.setEnabled(false);
        }
    }
}
