package de.df.uistate.handlers;

import java.awt.Component;
import java.awt.event.*;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.*;

import de.df.uistate.UIStateManager;

public class JDesktopPaneStateHandler extends AbstractStateHandler {
    private static final class DesktopListener implements ContainerListener, HierarchyListener, ComponentListener {
        public void componentAdded(ContainerEvent e) {
            JInternalFrame iframe = null;
            if ((e.getChild() instanceof JInternalFrame.JDesktopIcon)) {
                iframe = ((JInternalFrame.JDesktopIcon) e.getChild()).getInternalFrame();
                iframe.addComponentListener(this);
            } else {
                iframe = (JInternalFrame) e.getChild();
                if (!Arrays.asList(iframe.getComponentListeners()).contains(this)) {
                    iframe.addComponentListener(this);
                    UIStateManager.restore(iframe);
                }
            }
        }

        public void componentRemoved(ContainerEvent e) {
            if ((e.getChild() instanceof JInternalFrame.JDesktopIcon)) {
                return;
            }
            UIStateManager.store(e.getChild());
            e.getChild().removeComponentListener(this);
        }

        public void hierarchyChanged(HierarchyEvent e) {
            if (((e.getChangeFlags() & 1L) != 0L) && (e.getChangedParent() == null)) {
                ((JDesktopPane) e.getChanged()).removeHierarchyListener(this);
                ((JDesktopPane) e.getChanged()).removeContainerListener(this);
            }
        }

        public void componentHidden(ComponentEvent e) {
            UIStateManager.store(e.getComponent());
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void install(JDesktopPane desktop) {
            desktop.addContainerListener(this);
            desktop.addHierarchyListener(this);
        }
    }

    private static final DesktopListener DESKTOP_LISTENER = new DesktopListener();

    public JDesktopPaneStateHandler() {
        super(JDesktopPane.class);
    }

    public String prepare(Component comp, boolean restore, String recursePath) {
        if (restore) {
            DESKTOP_LISTENER.install((JDesktopPane) comp);
        }
        return null;
    }

    public void restore(Component comp, Preferences prefs) {
    }

    public void store(Component comp, Preferences prefs) {
    }
}
