package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.genedetailquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GeneDetailsQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GeneId;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GenePathQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class GeneDetailsQueryDialog extends JDialog {

    private boolean ok;
    private GenePathQuery genePathQuery;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel geneIdLabel;
    private JTextField geneId;
    private JPanel panel;

    public GeneDetailsQueryDialog(JFrame jFrame) {
        super(jFrame);
    }

    public void showDialog() {

        okButton = new JButton("Ok");
        okButton.addActionListener(this::ok);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::cancel);
        geneIdLabel = new JLabel("Gene ID");
        geneId = new JTextField();
        setLayout(new GridLayout(2,2));
        add(geneIdLabel);
        add(geneId);
        add(cancelButton);
        add(okButton);

        DialogMethods.center(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(true);
        pack();
        setVisible(true);
    }

    private void cancel(ActionEvent actionEvent) {
        ok = false;
        dispose();
    }

    private void ok(ActionEvent actionEvent) {
        ok = true;
        dispose();
    }

    public boolean isOk() {
        return ok;
    }

    public GeneDetailsQuery getGeneDetailsQuery() {
        return new GeneDetailsQuery(Arrays.asList(new GeneId(geneId.getText())));
    }
}
