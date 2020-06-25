package de.df.uistate.handlers;

import java.awt.Component;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;

public class JInternalFrameStateHandler extends AbstractStateHandler {
    public JInternalFrameStateHandler() {
        super(JInternalFrame.class);
    }

    public JInternalFrameStateHandler(Class<?> clazz) {
        super(clazz);
    }

    public void restore(Component comp, Preferences prefs) {
        JInternalFrame iframe = (JInternalFrame) comp;

        boolean isIconified = prefs.getBoolean("iconified", false);
        boolean isMaximized = prefs.getBoolean("maximized", false);
        if ((!iframe.isMaximum()) && (!iframe.isIcon())) {
            int x = prefs.getInt("x", -1);
            int y = prefs.getInt("y", -1);
            int width = prefs.getInt("width", -1);
            int height = prefs.getInt("height", -1);
            if (((x != -1) || (y != -1)) && (width >= 0) && (height >= 0)) {
                iframe.setBounds(x, y, width, height);
            }
        }
        try {
            iframe.setMaximum(isMaximized);
            iframe.setIcon(isIconified);
        } catch (PropertyVetoException e) {
        }
    }

    public void store(Component comp, Preferences prefs) {
        JInternalFrame iframe = (JInternalFrame) comp;

        prefs.putBoolean("maximized", iframe.isMaximum());
        prefs.putBoolean("iconified", iframe.isIcon());
        if ((!iframe.isMaximum()) && (!iframe.isIcon())) {
            Rectangle r = iframe.getBounds();
            prefs.putInt("x", r.x);
            prefs.putInt("y", r.y);
            prefs.putInt("width", r.width);
            prefs.putInt("height", r.height);
        }
    }
}
