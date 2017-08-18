package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class EditQueryTemplateDialog extends JDialog {

    public void showDialog() {
        setTitle("Edit Query");

        EditQueryPanel editQueryPanel = new EditQueryPanel();
        ParametersPanel parametersPanel = new ParametersPanel();
        NodeMappingPanel nodeMappingPanel = new NodeMappingPanel();
        EdgeMappingPanel edgeMappingPanel = new EdgeMappingPanel();
        ButtonPanel buttonPanel = new ButtonPanel((ev) -> this.dispose(), (ev) -> this.dispose());

        setLayout(new FlowLayout());
        add(editQueryPanel);
        add(parametersPanel);
        add(nodeMappingPanel);
        add(edgeMappingPanel);
        add(buttonPanel);

        DialogMethods.centerAndShow(this);
    }

    private final class ButtonPanel extends JPanel {

        ButtonPanel(ActionListener okHandler, ActionListener cancelHandler) {
            JButton cancel = new JButton("Cancel");
            JButton ok = new JButton("Ok");
            ok.addActionListener(okHandler);
            cancel.addActionListener(cancelHandler);
            add(cancel);
            add(ok);
        }
    }

    private final class EditQueryPanel extends JPanel {
        EditQueryPanel() {
            JTextArea textArea = new JTextArea(20,40);
            add(textArea);
        }
    }

    private final class ParametersPanel extends JPanel {
        ParametersPanel() {
            JTable jTable = new JTable();
            jTable.setModel(new ParameterTableModel ());
            jTable.getModel().setValueAt("Parameter", 0, 0);
            add(jTable);
        }
        private final class ParameterTableModel extends DefaultTableModel {

            public ParameterTableModel() {
                super(new String[] {"Parameter","Type"}, 5);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return (row > 0) && super.isCellEditable(row, column);
            }
        }

    }

    private final class NodeMappingPanel extends JPanel {
        NodeMappingPanel() {
            JTable jTable = new JTable();
            jTable.setModel(new DefaultTableModel(10,4));
            add(jTable);
        }
    }

    private final class EdgeMappingPanel extends JPanel {
        EdgeMappingPanel() {
            JTable jTable = new JTable();
            jTable.setModel(new DefaultTableModel(10, 4));
            add(jTable);
        }
    }


    public static void main(String[] args) {
        EditQueryTemplateDialog dialog = new EditQueryTemplateDialog();
        dialog.showDialog();
    }
}
