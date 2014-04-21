package com.tomecode.jdev.extension12g.showmepassword;

import java.util.List;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.wizard.Wizard;


/**
 * @author Tomas (Tome) Frastia
 * @see http://www.tomecode.com
 */
public class ShowMePasswordJdev12gWizard extends Wizard{
    public ShowMePasswordJdev12gWizard() {
        super();
    }

    @Override
    public boolean isAvailable(Context context) {
    return true;
    }

    @Override
    public String getShortLabel() {
    return "Show Me Password"   ; }

    @Override
    public boolean invoke(Context context) {
        try{
            List<Login> logins = JDeveloperConnectionsParser.parseConnection();
            PasswordWindow.showMe(logins);            
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }
 }

