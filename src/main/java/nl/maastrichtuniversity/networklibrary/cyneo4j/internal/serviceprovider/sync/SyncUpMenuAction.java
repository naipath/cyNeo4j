package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.sync;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SyncUpMenuAction extends AbstractCyAction {

    private final static String MENU_TITLE = "Sync Up";
    private final static String MENU_LOC = "Apps.cyNeo4j";

    private Plugin plugin;

    public SyncUpMenuAction(CyApplicationManager cyApplicationManager, Plugin plugin) {
        super(MENU_TITLE, cyApplicationManager, null, null);
        setPreferredMenu(MENU_LOC);
        setEnabled(false);
        setMenuGravity(0.1f);
        this.plugin = plugin;
    }

    protected Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!plugin.getInteractor().isConnected()) {
            JOptionPane.showMessageDialog(null, "Not connected to any remote instance");
            return;
        }
        getPlugin().getInteractor().syncUp(true, getPlugin().getCyApplicationManager().getCurrentNetwork());
    }
}
