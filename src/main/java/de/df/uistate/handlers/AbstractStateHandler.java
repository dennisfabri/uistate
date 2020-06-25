package de.df.uistate.handlers;

import de.df.uistate.UIStateHandler;
import java.awt.Component;

public abstract class AbstractStateHandler implements UIStateHandler {

    private Class<?> _handledClass;

    protected AbstractStateHandler(Class<?> handledClass) {
        this._handledClass = handledClass;
    }

    public String prepare(Component comp, boolean restore, String recursePath) {
        return recursePath;
    }

    public boolean doRecurse(Component comp, boolean restore) {
        return true;
    }

    public Class<?> getHandledClass() {
        return this._handledClass;
    }
}