package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.genepathquery;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GeneId;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery.GenePathQuery;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GenePathQueryDialog extends JDialog {

    private boolean ok;
    private GenePathQuery genePathQuery;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel fromGeneIdLabel;
    private JTextField fromGeneId;
    private JLabel toGeneLabel;
    private JTextField toGeneId;
    private JPanel panel;

    public GenePathQueryDialog(JFrame jFrame) {
        super(jFrame);
    }

    public void showDialog() {

        okButton = new JButton("Ok");
        okButton.addActionListener(this::ok);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::cancel);
        fromGeneIdLabel = new JLabel("from");
        fromGeneId = new JTextField();
        toGeneLabel = new JLabel("to");
        toGeneId = new JTextField();
        setLayout(new GridLayout(3,2));
        add(fromGeneIdLabel);
        add(fromGeneId);
        add(toGeneLabel);
        add(toGeneId);
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

    public GenePathQuery getGenePathQuery() {
        return new GenePathQuery(new GeneId(fromGeneId.getText()), new GeneId(toGeneId.getText()));
    }
}
