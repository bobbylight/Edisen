package org.fife.edisen.ui;

import org.fife.ui.CustomizableToolBar;

class EdisenToolBar extends CustomizableToolBar {

    private final Edisen app;

    EdisenToolBar(Edisen app) {
        this.app = app;
        initUI();
        makeCustomizable();
        setFloatable(false);
    }

    private void initUI() {
        add(createButton(app.getAction(Actions.OPEN_PROJECT_ACTION_KEY)));
        addSeparator();
        add(createButton(app.getAction(Actions.OPEN_ACTION_KEY)));
        add(createButton(app.getAction(Actions.SAVE_ACTION_KEY)));
        addSeparator();
        add(createButton(app.getAction(Actions.FIND_ACTION_KEY)));
        add(createButton(app.getAction(Actions.REPLACE_ACTION_KEY)));
        addSeparator();
        add(createButton(app.getAction(Actions.COMPILE_ACTION_KEY)));
        add(createButton(app.getAction(Actions.EMULATE_ACTION_KEY)));
        addSeparator();
        add(createButton(app.getAction(Edisen.ABOUT_ACTION_KEY)));
    }
}
