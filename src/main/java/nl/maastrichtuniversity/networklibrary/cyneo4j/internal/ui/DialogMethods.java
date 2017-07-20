package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.ui;

import javax.swing.*;
import java.awt.*;

public class DialogMethods {

    public static void center(JDialog jDialog) {
        Point cyLocation = jDialog.getParent().getLocation();
        int height = jDialog.getParent().getHeight();
        int width = jDialog.getParent().getWidth();
        jDialog.setLocation(new Point(cyLocation.x + (width / 4), cyLocation.y + (height / 4)));
    }
}
