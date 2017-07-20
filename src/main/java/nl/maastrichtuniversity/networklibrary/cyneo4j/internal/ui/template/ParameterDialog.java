package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.template;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ParameterDialog extends JDialog {

    private Map<String, Class<?>> parameterTypes;
    private Map<String, Object> parameters;
    private boolean ok;

    public ParameterDialog(Frame owner, CypherQueryTemplate cypherQueryTemplate) {
        super(owner);
        this.parameterTypes = cypherQueryTemplate.getParameterTypes();
        this.parameters = new HashMap<>();
    }

    public void showDialog() {
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");

        JTable parameterTable = new JTable(getParametersAsRows(), new String[] {"Parameter", "Value"});
        parameterTable.getModel().addTableModelListener(e -> {
            String key = (String) parameterTable.getValueAt(e.getFirstRow(),0);
            Object value = ((TableModel) e.getSource()).getValueAt(e.getFirstRow(), e.getColumn());
            parameters.put(key, value);
        });

        okButton.addActionListener(e -> {

            for(int i = 0; i < parameterTable.getRowCount(); i ++) {
                String key = (String) parameterTable.getValueAt(i, 0);
                Object value = parameterTable.getValueAt(i, 1);
                parameters.put(key, value);
            }
            ok = true;
            ParameterDialog.this.dispose();
        });
        cancelButton.addActionListener(e -> {
            ok = false;
            ParameterDialog.this.dispose();
        });

        JPanel buttons = new JPanel();
        buttons.add(cancelButton);
        buttons.add(okButton);

        setLayout(new GridLayout(2,1));
        add(parameterTable);
        add(buttons);

        DialogMethods.center(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(true);
        pack();
        setVisible(true);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    private Object[][] getParametersAsRows() {

        Object[][] rows = new Object[parameterTypes.size()][2];
        int i = 0;
        for(Map.Entry<String,Class<?>> entry : parameterTypes.entrySet()) {
            rows[i][0] = entry.getKey();
            rows[i][1] = "";
            i++;
        }
        return rows;
    }

    public boolean isOk() {
        return ok;
    }
}
