package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.querytemplate;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.provider.CypherQueryTemplateDirectoryProvider;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.DialogMethods;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectTemplateDialog extends JDialog {


    private static final class TemplateQueryListEntry {
        final String id;
        final String name;
        private TemplateQueryListEntry(String id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    private final String[] visualStyles;
    private final CypherQueryTemplateDirectoryProvider provider;
    private String templateDir;
    private final Consumer<String> templateDirectoryListener;
    private boolean ok;
    private String networkName;
    private String visualStyle;
    private String cypherQueryTemplateId;

    public SelectTemplateDialog(JFrame jFrame, String[] visualStyles, String templateDir, Consumer<String> templateDirectoryListener) {
        super(jFrame);
        this.visualStyles = visualStyles;
        this.templateDir = templateDir;
        this.templateDirectoryListener = templateDirectoryListener;
        this.provider = CypherQueryTemplateDirectoryProvider.create();
    }

    public void showDialog() {

        setTitle("Select Query");

        TemplateQueryListEntry[] templateQueryListEntries = getQueryTemplatesFromDir();

        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        JTextField networkNameField = new JTextField();
        JLabel networkNameLabel = new JLabel("network");
        JComboBox visualStyleComboBox = new JComboBox(visualStyles);
        JLabel visualStyleLabel = new JLabel("Visual Style");
        SelectQueryPanel queryListPanel = new SelectQueryPanel(templateQueryListEntries);
        JLabel queryListLabel = new JLabel("Queries");

        okButton.addActionListener(e -> {
            networkName = networkNameField.getText();
            if(visualStyleComboBox.getSelectedIndex()== -1) {
                JOptionPane.showMessageDialog(this, "No visualstyle selected", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            visualStyle = visualStyleComboBox.getSelectedItem().toString();
            if(queryListPanel.getQueryList().getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "No query selected", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cypherQueryTemplateId = ((TemplateQueryListEntry)queryListPanel.getQueryList().getSelectedValue()).id;
            ok = true;
            SelectTemplateDialog.this.dispose();
        });

        cancelButton.addActionListener(e -> {
            ok = false;
            SelectTemplateDialog.this.dispose();
        });

        JPanel topPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(networkNameLabel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        topPanel.add(networkNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(visualStyleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        topPanel.add(visualStyleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        topPanel.add(queryListLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        topPanel.add(queryListPanel, gbc);

        add(topPanel);
        add(buttonPanel,  BorderLayout.SOUTH);

        DialogMethods.centerAndShow(this);
    }


    public boolean isOk() {
        return ok;
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getVisualStyle() {
        return visualStyle;
    }

    public CypherQueryTemplate getCypherQueryTemplate() {
        return provider.getCypherQueryTemplate(Long.valueOf(cypherQueryTemplateId)).orElse(null);
    }

    public String getTemplateDir() {
        return templateDir;
    }

    private TemplateQueryListEntry[] getQueryTemplatesFromDir() {

        if(templateDir == null || templateDir.isEmpty()) {
            templateDir = selectQueryFolder();
        }
        if(templateDir == null || templateDir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No template directory selected");
            return new TemplateQueryListEntry[0];
        }
        Path templateDirectory = Paths.get(templateDir);
        provider.readDirectory(templateDirectory);

        TemplateQueryListEntry[] items = getAllTemplates();
        if(items == null || items.length == 0 ) {
            JOptionPane.showMessageDialog(this, "No queries found in the directory");
            return new TemplateQueryListEntry[] {};
        }
        if(templateDirectoryListener != null) {
            templateDirectoryListener.accept(templateDir);
        }
        return items;
    }

    private String selectQueryFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();

        } else {
            return null;
        }
    }

    private TemplateQueryListEntry[] getAllTemplates() {
        return provider.getCypherQueryTemplateMap().entrySet()
                .stream()
                .map(entry -> new TemplateQueryListEntry(String.valueOf(entry.getKey()), entry.getValue().getName()))
                .collect(Collectors.toList())
                .toArray(new TemplateQueryListEntry[0]);
    }

    private final class SelectQueryPanel extends JPanel {

        JList queryList;

        public SelectQueryPanel(TemplateQueryListEntry[] templateQueryListEntries) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JScrollPane queryListPane = new JScrollPane();
            queryList = new JList(templateQueryListEntries);
            queryList.setFixedCellWidth(200);
            queryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            queryListPane.setViewportView(queryList);
            JPanel queryListButtonPanel = new JPanel();
            JButton newQueryButton = new JButton("New");
            JButton editQueryButton = new JButton("Edit");
            JButton selectFolderButton = new JButton("Select Folder");
            selectFolderButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
            selectFolderButton.addActionListener(e -> {
                templateDir = selectQueryFolder();
                TemplateQueryListEntry[] items = getQueryTemplatesFromDir();
                queryList.setListData(items);
            });
            add(queryListPane);
            queryListButtonPanel.add(newQueryButton);
            queryListButtonPanel.add(editQueryButton);
            queryListButtonPanel.add(selectFolderButton);
            add(queryListButtonPanel);
        }

        public JList getQueryList() {
            return queryList;
        }
    }




    public static void main(String[] args) {
        SelectTemplateDialog dialog = new SelectTemplateDialog(null,
                new String[]{"v1","v2"},
                null,
                templateDir -> System.out.println(templateDir)
                );
        dialog.showDialog();
    }

}
