package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.util.prefs.Preferences;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class JTableStateHandler extends AbstractStateHandler {
    public JTableStateHandler() {
        super(JTable.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JTable table = (JTable) comp;
        TableColumnModel cm = table.getColumnModel();
        ListSelectionModel csm = cm.getSelectionModel();
        TableColumn[] cols = new TableColumn[cm.getColumnCount()];
        for (int i = cols.length - 1; i >= 0; i--) {
            cols[i] = cm.getColumn(i);
            cm.removeColumn(cols[i]);
        }
        for (int i = 0; i < cols.length; i++) {
            TableColumn col = cols[prefs.getInt("col" + i + ".index", i)];
            cm.addColumn(col);
            col.setPreferredWidth(prefs.getInt("col" + i + ".width", col.getWidth()));
            if (prefs.getBoolean("col" + i + ".sel", false)) {
                csm.addSelectionInterval(i, i);
            }
        }

        ListSelectionModel rsm = table.getSelectionModel();
        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            if (prefs.getBoolean("row" + i + ".sel", false)) {
                rsm.addSelectionInterval(i, i);
            }
        }
    }

    public void store(Component comp, Preferences prefs) {
        JTable table = (JTable) comp;
        TableColumnModel cm = table.getColumnModel();
        ListSelectionModel csm = cm.getSelectionModel();

        for (int i = cm.getColumnCount() - 1; i >= 0; i--) {
            TableColumn col = cm.getColumn(i);
            prefs.putInt("col" + i + ".index", col.getModelIndex());
            prefs.putInt("col" + i + ".width", col.getWidth());
            prefs.putBoolean("col" + i + ".sel", csm.isSelectedIndex(i));
        }
        ListSelectionModel rsm = table.getSelectionModel();

        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            prefs.putBoolean("row" + i + ".sel", rsm.isSelectedIndex(i));
        }
    }
}