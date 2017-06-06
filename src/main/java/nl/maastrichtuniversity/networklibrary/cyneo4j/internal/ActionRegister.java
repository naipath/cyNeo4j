package nl.maastrichtuniversity.networklibrary.cyneo4j.internal;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 04-06-17.
 */
public class ActionRegister {

    private final List<AbstractCyAction> registeredActions;
    private final CySwingApplication cySwingApplication;

    public ActionRegister(CySwingApplication cySwingApplication) {
        this.registeredActions = new ArrayList<>();
        this.cySwingApplication = cySwingApplication;
    }

    public static ActionRegister create(ServiceLocator serviceLocator) {
        return new ActionRegister(serviceLocator.getService(CySwingApplication.class));
    }


    void cleanUp() {
        //extension actions
        unregisterActions();
    }

    public void registerAction(AbstractCyAction action) {
        registeredActions.add(action);
        cySwingApplication.addAction(action);
    }

    public void unregisterActions() {
        registeredActions.forEach(cyAction -> getCySwingApplication().removeAction(cyAction));
    }

    private CySwingApplication getCySwingApplication() {
        return cySwingApplication;
    }
}
