package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.CommandFactory;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.CommandRunner;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.CopyDataTask;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.Neo4jClient;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.work.AbstractTask;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

public class CommandMenuAction extends AbstractCyAction {

    private final static String MENU_LOC = "Apps.cyNeo4j";
    private final Neo4jClient neo4JClient;
    private final CommandRunner commandRunner;
    private final Supplier<AbstractTask> taskSupplier;

    public static CommandMenuAction create(String menuTitle, Services services, Supplier<AbstractTask> taskSupplier) {
        return new CommandMenuAction(menuTitle, services, taskSupplier);
    }

    private CommandMenuAction(String menuTitle, Services services, Supplier<AbstractTask> taskSupplier) {
        super(menuTitle);
        this.taskSupplier = taskSupplier;
        this.commandRunner = services.getCommandRunner();
        this.neo4JClient = services.getNeo4jClient();
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!neo4JClient.isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
            return;
        }
        AbstractTask abstractTask = taskSupplier.get();
        commandRunner.execute(abstractTask);
    }
}
