package org.lisasp.legacy.uistate;

import java.awt.Component;
import java.util.prefs.Preferences;

public interface UIStateHandler {
    public void restore(Component paramComponent, Preferences paramPreferences);

    public void store(Component paramComponent, Preferences paramPreferences);

    public String prepare(Component paramComponent, boolean paramBoolean, String paramString);

    public boolean doRecurse(Component paramComponent, boolean paramBoolean);

    public Class<?> getHandledClass();
}