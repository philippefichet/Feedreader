package controllers;

import play.mvc.Http.Context;

public class SecuredUser extends SecuredAbstract {

    @Override
    public String getUsername(Context ctx) {
        String username = getUsername(ctx, "security.admin.login", "security.admin.password");
        if (username == null) {
            return getUsername(ctx, "security.user.login", "security.user.password");
        } else {
            return username;
        }
    }
}
