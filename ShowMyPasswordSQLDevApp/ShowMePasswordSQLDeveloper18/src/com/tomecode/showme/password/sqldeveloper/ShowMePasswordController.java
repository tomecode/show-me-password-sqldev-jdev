package com.tomecode.showme.password.sqldeveloper;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;


/**
 * Controller for action com.tomecode.showme.password.sqldeveloper.showmepassword.
 *
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public final class ShowMePasswordController implements Controller {

    public static final int SHOW_ME_CMD_ID = Ide.findCmdID("com.tomecode.showme.password.sqldeveloper.showmepassword");

    public boolean update(IdeAction action, Context context) {
        int id = action.getCommandId();
        if (id == SHOW_ME_CMD_ID) {
            action.setEnabled(true);
            return true;
        }
        return true;
    }

    public boolean handleEvent(IdeAction action, Context context) {
        int id = action.getCommandId();
        if (id == SHOW_ME_CMD_ID) {
            PasswordWindow.showMe(SQLDeveloperConnectionsParser.loadDbConfig());
            return true;
        }
        return false;
    }
}
