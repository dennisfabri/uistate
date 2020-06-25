package de.df.uistate;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.*;

import de.df.uistate.handlers.*;

public class UIStateManager {
    private static final class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            UIStateManager.store(e.getWindow());
            e.getWindow().removeWindowListener(this);
        }

        public void windowClosed(WindowEvent e) {
            UIStateManager.store(e.getWindow());
            e.getWindow().removeWindowListener(this);
        }
    }

    private static final ClosingListener               CLOSING_LISTENER = new ClosingListener();

    private static Map<Class<?>, List<UIStateHandler>> _handlers        = new HashMap<>();
    private static List<UIStateHandler>                _defaultHandlers = new ArrayList<>(Arrays.asList(
            new UIStateHandler[] { new WindowStateHandler(), new JSplitPaneStateHandler(), new JViewportStateHandler(),
                    new JTreeStateHandler(), new JListStateHandler(), new JTableStateHandler(),
                    new JTabbedPaneStateHandler(), new JDesktopPaneStateHandler(), new JInternalFrameStateHandler() }));

    private static boolean                             _initialized;

    private static Preferences                         _prefs;

    private static void init() {
        if (!_initialized) {
            _initialized = true;
            for (UIStateHandler h : _defaultHandlers) {
                registerHandler(h);
            }
        }
    }

    public static List<UIStateHandler> getDefaultHandlers() {
        return _defaultHandlers;
    }

    public static void setDefaultHandlers(List<UIStateHandler> handlers) {
        _defaultHandlers = handlers;
    }

    public static void manage(Window win, Object id) {
        setID(win, id);
        manage(win);
    }

    public static void manage(Window win, Preferences prefs) {
        if (prefs == null) {
            throw new IllegalArgumentException("null preferences not allowed");
        }
        setParentPreferences(win, prefs);
        manage(win);
    }

    public static void manage(Window win, Object id, Preferences prefs) {
        setID(win, id);
        manage(win, prefs);
    }

    public static void manage(Window win) {
        win.addWindowListener(CLOSING_LISTENER);
        restore(win);
    }

    public static void restore(Component comp, Preferences prefs) {
        if (prefs == null) {
            throw new IllegalArgumentException("null preferences not allowed");
        }
        setParentPreferences(comp, prefs);
        restore(comp);
    }

    public static void restore(Component comp, Object id) {
        setID(comp, id);
        restore(comp);
    }

    public static void restore(Component comp, Object id, Preferences prefs) {
        if (prefs == null) {
            throw new IllegalArgumentException("null preferences not allowed");
        }
        setID(comp, id);
        setParentPreferences(comp, prefs);
        restore(comp);
    }

    public static void restore(Component comp) {
        update(comp, getParentPreferences(comp), null, getDefaultID(comp), true);
    }

    public static void store(Component comp) {
        update(comp, getParentPreferences(comp), null, getDefaultID(comp), false);
    }

    public static boolean update(Component comp, Preferences prefs, String parentPath, String defaultID,
            boolean restore) {
        init();
        boolean updated = false;
        if (prefs == null) {
            throw new IllegalArgumentException("null preferences not allowed");
        }
        String id = getID(comp, defaultID);
        try {
            String path = parentPath + "/" + id;
            String recursePath = path;
            UIStateHandler[] handlers = getHandlers(comp);
            boolean recurse = true;
            for (int i = 0; i < handlers.length; i++) {
                recursePath = handlers[i].prepare(comp, restore, recursePath);
                recurse &= handlers[i].doRecurse(comp, restore);
            }

            if ((id == null) || ((restore) && (!prefs.nodeExists(path)))) {
                return false;
            }

            if (restore) {
                setID(comp, id);
                setParentPreferences(comp, prefs, parentPath);
            }

            if (recurse) {
                if ((comp instanceof RootPaneContainer)) {
                    Container contentPane = ((RootPaneContainer) comp).getContentPane();
                    defaultID = getDefaultID(contentPane);
                    if (defaultID == null) {
                        defaultID = "content";
                    }
                    updated |= update(contentPane, prefs, recursePath, defaultID, restore);
                } else if ((comp instanceof Container)) {
                    Component[] children = ((Container) comp).getComponents();
                    for (int i = 0; i < children.length; i++) {
                        defaultID = getDefaultID(children[i]);
                        if (defaultID == null) {
                            defaultID = String.valueOf(i);
                        }
                        updated |= update(children[i], prefs, recursePath, defaultID, restore);
                    }
                }
            }

            if (handlers.length > 0) {
                updated = true;
                prefs = prefs.node(path);
                for (int i = 0; i < handlers.length; i++) {
                    if (restore) {
                        handlers[i].restore(comp, prefs);
                    } else {
                        handlers[i].store(comp, prefs);
                    }
                }
            }

            return updated;
        } catch (Exception e) {
        }
        return updated;
    }

    private static UIStateHandler[] getHandlers(Component comp) {
        List<UIStateHandler> result = new ArrayList<>();
        for (Class<?> cl = comp.getClass(); cl != null; cl = cl.getSuperclass()) {
            List<UIStateHandler> l = _handlers.get(cl);
            if (l != null) {
                result.addAll(l);
                break;
            }
        }
        return (UIStateHandler[]) result.toArray(new UIStateHandler[result.size()]);
    }

    public static void registerHandler(UIStateHandler handler) {
        List<UIStateHandler> h = _handlers.get(handler.getHandledClass());
        if (h == null) {
            h = new ArrayList<>();
            _handlers.put(handler.getHandledClass(), h);
        }
        h.add(handler);
    }

    private static String getDefaultID(Component comp) {
        if ((comp instanceof Frame))
            return ((Frame) comp).getTitle();
        if ((comp instanceof Dialog))
            return ((Dialog) comp).getTitle();
        if ((comp instanceof JInternalFrame)) {
            return ((JInternalFrame) comp).getTitle();
        }
        return null;
    }

    public static String getID(Component comp, String def) {
        Object id = getClientProperty(comp, "uipref.id");
        if (id == null) {
            id = def;
            if (id == null) {
                return null;
            }
        }
        id = id.toString();
        return ((String) id).replace('/', '\\');
    }

    public static void setID(Component comp, Object id) {
        putClientProperty(comp, "uipref.id", id);
    }

    public static void setParentPreferences(Component comp, Preferences prefs) {
        setParentPreferences(comp, prefs, null);
    }

    private static void setParentPreferences(Component comp, Preferences prefs, String parentPath) {
        putClientProperty(comp, "uipref.parent.pref", prefs);
        putClientProperty(comp, "uipref.parent.pref.path", parentPath);
    }

    public static Preferences getParentPreferences(Component comp) {
        return getParentPreferences(comp, getPreferences());
    }

    public static Preferences getParentPreferences(Component comp, Preferences def) {
        Preferences prefs = (Preferences) getClientProperty(comp, "uipref.parent.pref");
        if (prefs == null) {
            prefs = def;
        }
        String path = (String) getClientProperty(comp, "uipref.parent.pref.path");
        if (path != null) {
            prefs = prefs.node(path);
        }
        return prefs;
    }

    public static Preferences getPreferences(Component comp) {
        return getPreferences(comp, getPreferences());
    }

    public static Preferences getPreferences(Component comp, Preferences parentDefault) {
        String id = getID(comp, getDefaultID(comp));
        if (id == null) {
            return null;
        }
        return getParentPreferences(comp, parentDefault).node(id);
    }

    public static Preferences getPreferences() {
        if (_prefs == null) {
            _prefs = getDefaultPreferences();
        }
        return _prefs;
    }

    public static void setPreferences(Preferences prefs) {
        _prefs = prefs;
    }

    private static Preferences getDefaultPreferences() {
        return Preferences.userRoot().node("uistate");
    }

    private static void putClientProperty(Component comp, Object key, Object value) {
        if ((comp instanceof JComponent)) {
            ((JComponent) comp).putClientProperty(key, value);
        } else if ((comp instanceof RootPaneContainer)) {
            ((RootPaneContainer) comp).getRootPane().putClientProperty(key, value);
        } else {
            throw new IllegalArgumentException("unable to set client property on " + comp);
        }
    }

    private static Object getClientProperty(Component comp, Object key) {
        if ((comp instanceof JComponent))
            return ((JComponent) comp).getClientProperty(key);
        if ((comp instanceof RootPaneContainer)) {
            return ((RootPaneContainer) comp).getRootPane().getClientProperty(key);
        }
        return null;
    }
}
