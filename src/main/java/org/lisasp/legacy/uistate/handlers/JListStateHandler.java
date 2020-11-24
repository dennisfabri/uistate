package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.util.prefs.Preferences;

import javax.swing.JList;

public class JListStateHandler extends AbstractStateHandler {
    public JListStateHandler() {
        super(JList.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JList<?> list = (JList<?>) comp;
        int length = prefs.getInt("list.selNumber", 0);
        int[] sels = new int[length];
        for (int i = 0; i < sels.length; i++) {
            sels[i] = prefs.getInt("list.sel" + i, -1);
        }
        list.setSelectedIndices(sels);
    }

    public void store(Component comp, Preferences prefs) {
        JList<?> list = (JList<?>) comp;
        int[] sels = list.getSelectedIndices();
        prefs.putInt("list.selNumber", sels.length);
        for (int i = 0; i < sels.length; i++) {
            prefs.putInt("list.sel" + i, sels[i]);
        }
    }
}
