package com.tomecode.jdev.extension12g.showmepassword;

import oracle.ide.Context;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.IdeAction;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */

public class ShowMePasswordJdev12gMenu  implements ContextMenuListener {
    public ShowMePasswordJdev12gMenu() {
        super();
    }

    @Override
    public void menuWillShow(ContextMenu contextMenu) {
        IdeAction action = IdeAction.find(ShowMePasswordJdev12gControler.CMD_ID);

        // Then add it to the context menu.
        contextMenu.add(contextMenu.createMenuItem(action));
    }

    @Override
    public void menuWillHide(ContextMenu contextMenu) {
        // TODO Implement this method
    }

    @Override
    public boolean handleDefaultAction(Context context) {
        // TODO Implement this method
        return false;
    }
}
