# uistate
Stores the state of UIComponents in swing applications

## Origin

UIState was developed by jayasoft.fr and published on their homepage. It has not been actively developed for quite a few years and the homepage is no longer directly available. Parts of the homepage can still be reached as a copy at <http://www.jaya.free.fr/uistate.html>.

This version is based on the version 0.6 and has little to no modifications but is now compiled using maven and Java 11.

## Cite from the original homepage

UIState is a non-intrusive user interface state manager for java Swing rich client applications.

UIState allow application to restore their gui state during application use and between multiple application session. For example, if your application use JInternalFrame to present MDI interface, UIState will manage for you the position and the size of the frame. Moreover UIState will also recover, if needed, the state of the widgets inside this frame. For example, if your frame display JSplitPane, UIState will also restore the exact position of the separator.

UIState is extensible and allow you to handle "home made" component via external handler registration. You only need to implement two methods to fit UIState requirement. UIState is easy to integrate in existing application only one code line is needed. Just locate where you create JFrame, JDialog, JWindow,... any of Window subclasses and add the following code UIStateManager.manage(..).

UIState comes with handler for common swing components JFrame, JWindow, JInternalFrame, JTree, JTable, JList, JSplitPane and theirs respectives properties.(size, position, selection, ..). Note that UIState do not handle data model. You still have to open internal frame, build tableModel, etc.

### Features

- easy to use and integrate
- powerfull
- non-intrusive
- easily extensible
- based on java.util.Preference
- swing component ready!
- free and open source!
