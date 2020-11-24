package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.util.prefs.Preferences;

import javax.swing.JTabbedPane;

public class JTabbedPaneStateHandler extends AbstractStateHandler {
    public JTabbedPaneStateHandler() {
        super(JTabbedPane.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JTabbedPane tabbedPane = (JTabbedPane) comp;
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(prefs.getInt("tab.sel", 0));
        }
    }

    public void store(Component comp, Preferences prefs) {
        JTabbedPane tabbedPane = (JTabbedPane) comp;
        prefs.putInt("tab.sel", tabbedPane.getSelectedIndex());
    }
}