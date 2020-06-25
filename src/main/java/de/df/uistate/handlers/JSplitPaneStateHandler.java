package de.df.uistate.handlers;

import java.awt.Component;
import java.util.prefs.Preferences;
import javax.swing.JSplitPane;

public class JSplitPaneStateHandler extends AbstractStateHandler {
    public JSplitPaneStateHandler() {
        super(JSplitPane.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JSplitPane split = (JSplitPane) comp;
        int dividerLocation = prefs.getInt("dividerLocation", -1);
        if (dividerLocation != -1) {
            split.setDividerLocation(dividerLocation);
        }
    }

    public void store(Component comp, Preferences prefs) {
        JSplitPane split = (JSplitPane) comp;
        prefs.putInt("dividerLocation", split.getDividerLocation());
    }
}