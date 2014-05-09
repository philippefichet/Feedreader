package controllers;

import play.mvc.Http.Context;

public class SecuredAdmin extends SecuredAbstract {

    @Override
    public String getUsername(Context ctx) {
        return getUsername(ctx, "security.admin.login", "security.admin.password");
    }
}
