package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.cypherquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import java.awt.*;

public class CypherQueryDialog extends JDialog {

    private final static String INITIAL_QUERY = "match (n)-[r]->(m) return n,r,m";
    private String cypherQuery;
    private boolean executeQuery;

    public CypherQueryDialog(Frame owner) {
        super(owner);
    }

    public void showDialog() {

        JTextArea queryText = new JTextArea(20,80);
        queryText.setText(INITIAL_QUERY);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            executeQuery = false;
            CypherQueryDialog.this.dispose();
        });
        JButton executButton = new JButton("Execute Query");
        executButton.addActionListener(e ->{
            executeQuery = true;
            cypherQuery = queryText.getText();
            CypherQueryDialog.this.dispose();
        });

        JPanel topPanel =new JPanel();
        JPanel buttonPanel =new JPanel();

        topPanel.add(queryText);
        buttonPanel.add(cancelButton);
        buttonPanel.add(executButton);
        add(topPanel);
        add(buttonPanel,  BorderLayout.SOUTH);

        DialogMethods.center(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(true);
        pack();
        setVisible(true);
    }

    public String getCypherQuery() {
        return cypherQuery;
    }

    public boolean isExecuteQuery() {
        return executeQuery;
    }

    public static void main(String[] args) {
        CypherQueryDialog dialog = new CypherQueryDialog(null);
        dialog.showDialog();
    }

}
