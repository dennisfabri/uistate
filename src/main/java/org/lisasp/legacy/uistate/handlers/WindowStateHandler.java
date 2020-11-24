package org.lisasp.legacy.uistate.handlers;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.prefs.Preferences;

public class WindowStateHandler extends AbstractStateHandler {
    public WindowStateHandler() {
        super(Window.class);
    }

    public void restore(Component comp, Preferences prefs) {
        Window win = (Window) comp;
        int x = prefs.getInt("x", -1);
        int y = prefs.getInt("y", -1);
        int width = prefs.getInt("width", -1);
        int height = prefs.getInt("height", -1);
        if ((x != -1) || (y != -1) || (width != -1) || (height != -1)) {
            win.setBounds(x, y, width, height);
        }
        if ((win instanceof Frame)) {
            Frame frame = (Frame) win;
            frame.setExtendedState(prefs.getInt("extendedState", 0));
        }
    }

    public void store(Component comp, Preferences prefs) {
        Window win = (Window) comp;
        if ((win instanceof Frame)) {
            Frame frame = (Frame) win;
            prefs.putInt("extendedState", frame.getExtendedState());
            if (frame.getExtendedState() != 0) {
                return;
            }
        }
        Rectangle r = win.getBounds();
        prefs.putInt("x", r.x);
        prefs.putInt("y", r.y);
        prefs.putInt("width", r.width);
        prefs.putInt("height", r.height);
    }
}