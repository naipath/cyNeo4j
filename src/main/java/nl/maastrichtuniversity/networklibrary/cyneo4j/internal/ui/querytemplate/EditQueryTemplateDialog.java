package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CopyAllMappingStrategy;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

public class EditQueryTemplateDialog extends JDialog {

    private String cypherQuery;
    private EditQueryPanel editQueryPanel;
    private ParametersPanel parametersPanel;

    public void showDialog() {
        setTitle("Edit Query");

        editQueryPanel = new EditQueryPanel();

        parametersPanel = new ParametersPanel();
        ButtonPanel buttonPanel = new ButtonPanel((ev) -> this.dispose(), (ev) -> this.dispose());

        JPanel panel = new JPanel();
        setLayout(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(editQueryPanel);
        panel.add(parametersPanel);
        panel.add(buttonPanel);
        add(panel);
        DialogMethods.centerAndShow(this);
    }

    public CypherQueryTemplate createCypherQuery() {
        CypherQueryTemplate.Builder builder = CypherQueryTemplate.builder();
        builder.setQueryTemplate(editQueryPanel.textArea.getText());
        builder.setName("Import");
        builder.addMappingStrategy(new CopyAllMappingStrategy("refid", "network"));

        for(int i = 1; i < parametersPanel.getRowCount(); i++) {
            String parameter = parametersPanel.getParameter(i);
            Class<?> parameterType = parametersPanel.getParameterType(i);
            if (parameter != null && parameterType != null) {
                builder.addParameter(parameter, parameterType);

            }
        }
        return builder.build();
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
        JTextArea textArea;
        EditQueryPanel() {
            textArea = new JTextArea(20,40);
            JLabel label = new JLabel("Cypher Query");
            setLayout(new BorderLayout());
            add(label, PAGE_START);
            add(textArea, CENTER);
        }

    }

    private final class ParametersPanel extends JPanel {

        private ParameterTableModel parameterTableModel =  new ParameterTableModel ();

        ParametersPanel() {
            JTable jTable = new JTable();
            jTable.setModel(parameterTableModel);
            jTable.getModel().setValueAt("Parametername", 0, 0);
            jTable.getModel().setValueAt("Type", 0, 1);

            TableColumn column = jTable.getColumnModel().getColumn(1);
            JComboBox comboBox = new JComboBox();
            comboBox.addItem("String");
            comboBox.addItem("Integer");
            comboBox.addItem("Long");
            comboBox.addItem("Double");
            column.setCellEditor(new DefaultCellEditor(comboBox));

            JLabel label = new JLabel("Parameters");
            setLayout(new BorderLayout());
            add(label, PAGE_START);
            add(jTable, CENTER);
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

        private int getRowCount() {
            return parameterTableModel.getRowCount();
        }

        private void addRow() {
            parameterTableModel.addRow(new Object[] {"param", "String"});
        }

         private String getParameter(int row) {
            return (String) parameterTableModel.getValueAt(row,0);
        }

        private Class<?> getParameterType(int row) {
            return getType((String) parameterTableModel.getValueAt(row,1));
        }

        private Class<?> getType(String type) {
            switch(type) {
                case "String" : return String.class;
                case "Integer" : return Integer.class;
                case "Long" : return Long.class;
                case "Double" : return Double.class;
                default: return null;
            }
        }
    }

    public static void main(String[] args) {
        EditQueryTemplateDialog dialog = new EditQueryTemplateDialog();
        dialog.showDialog();
    }
}
