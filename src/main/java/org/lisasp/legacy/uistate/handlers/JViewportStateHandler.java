package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.awt.Point;
import java.util.prefs.Preferences;

import javax.swing.JViewport;

public class JViewportStateHandler extends AbstractStateHandler {
    public JViewportStateHandler() {
        super(JViewport.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JViewport view = (JViewport) comp;
        int x = prefs.getInt("viewPosition.x", -1);
        int y = prefs.getInt("viewPosition.y", -1);
        if ((x != -1) || (y != -1)) {
            view.setViewPosition(new Point(x, y));
        }
    }

    public void store(Component comp, Preferences prefs) {
        JViewport view = (JViewport) comp;
        Point viewPosition = view.getViewPosition();
        prefs.putInt("viewPosition.x", viewPosition.x);
        prefs.putInt("viewPosition.y", viewPosition.y);
    }
}