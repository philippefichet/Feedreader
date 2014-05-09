package controllers;

import org.apache.commons.codec.binary.Base64;

import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security;

abstract public class SecuredAbstract extends Security.Authenticator {

    public String getUsername(Context ctx, String loginKey, String passwordKey) {
        String login = Configuration.root().getString(loginKey).trim();
        String password = Configuration.root().getString(passwordKey).trim();
        String auth = ctx.request().getHeader("Authorization");
        if (auth != null) {
            auth = auth.replace("Basic", "").trim();
            String usernamePassword = new String(Base64.decodeBase64(auth.getBytes()));
            String[] info = usernamePassword.split(":");
            if (info.length == 2 && info[0].equals(login) && info[1].equals(password)) {
                return info[0];
            } else {
                return null;
            }
        }
        return null;
    }

    public String getRealm() {
        return "Feedreader";
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        Response r = ctx.response();
        r.setHeader("Unauthorized", "401");
        r.setHeader("WWW-Authenticate", "Basic realm=\"" + getRealm() + "\"");
        return unauthorized("onUnauthorized");
    }
}
