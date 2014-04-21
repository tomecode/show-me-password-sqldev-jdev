package com.tomecode.jdev.extension12g.showmepassword;


import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.wizard.WizardManager;

/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */

public class ShowMePasswordJdev12gControler implements Controller {
    public ShowMePasswordJdev12gControler() {
        super();
    }

    public static final int CMD_ID =
        Ide.findCmdID("com.tomecode.jdev.extension12g.showmepassword.jdev12g.invokeAction");


    @Override
    public boolean handleEvent(IdeAction ideAction, Context context) {
        WizardManager.getInstance().getWizard(ShowMePasswordJdev12gWizard.class).invoke(context);
        return true;
    }

    @Override
    public boolean update(IdeAction ideAction, Context context) {
        ideAction.setEnabled(true);
        return true;
    }
}
