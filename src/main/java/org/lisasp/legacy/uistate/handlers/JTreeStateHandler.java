package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.util.prefs.Preferences;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeStateHandler extends AbstractStateHandler {
    public JTreeStateHandler() {
        super(JTree.class);
    }

    public void restore(Component comp, Preferences prefs) {
        JTree tree = (JTree) comp;
        TreeModel model = tree.getModel();
        restorePath(prefs, tree, model, new TreePath(model.getRoot()), "root");
    }

    public void store(Component comp, Preferences prefs) {
        JTree tree = (JTree) comp;
        TreeModel model = tree.getModel();
        storePath(prefs, tree, model, new TreePath(model.getRoot()), "root");
    }

    private void restorePath(Preferences prefs, JTree tree, TreeModel model, TreePath path, String prefix) {
        boolean isExpanded = prefs.getBoolean(prefix + ".exp", false);
        boolean isSelected = prefs.getBoolean(prefix + ".sel", false);
        if (isSelected) {
            tree.getSelectionModel().addSelectionPath(path);
        }
        if (isExpanded) {
            tree.expandPath(path);
            Object parent = path.getLastPathComponent();
            int number = model.getChildCount(parent);
            for (int i = 0; i < number; i++) {
                Object child = model.getChild(parent, i);
                restorePath(prefs, tree, model, path.pathByAddingChild(child), prefix + "." + i);
            }
        }
    }

    private void storePath(Preferences prefs, JTree tree, TreeModel model, TreePath path, String prefix) {
        boolean isExpanded = tree.isExpanded(path);
        prefs.putBoolean(prefix + ".exp", isExpanded);
        prefs.putBoolean(prefix + ".sel", tree.isPathSelected(path));
        if (isExpanded) {
            Object parent = path.getLastPathComponent();
            int number = model.getChildCount(parent);
            for (int i = 0; i < number; i++) {
                Object child = model.getChild(parent, i);
                storePath(prefs, tree, model, path.pathByAddingChild(child), prefix + "." + i);
            }
        }
    }
}