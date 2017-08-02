package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Services;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui.connect.ConnectToNeo4j;

import javax.swing.*;
import java.awt.*;

public class DialogMethods {

    public static void center(JDialog jDialog) {
        Point cyLocation = jDialog.getParent().getLocation();
        int height = jDialog.getParent().getHeight();
        int width = jDialog.getParent().getWidth();
        jDialog.setLocation(new Point(cyLocation.x + (width / 4), cyLocation.y + (height / 4)));
    }

    public static void connect(Services services) {
        if (!services.getNeo4jClient().isConnected()) {
            ConnectToNeo4j connectToNeo4j = ConnectToNeo4j.create(services);
            connectToNeo4j.connect();
        }
    }
}
