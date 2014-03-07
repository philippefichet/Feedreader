package controllers;

import org.apache.commons.codec.binary.Base64;

import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredAdmin extends SecuredAbstract {
    @Override
    public String getUsername(Context ctx) {
    	return getUsername(ctx, "security.admin.login", "security.admin.password");
    }

//    @Override
//    public Result onUnauthorized(Context ctx) {
//    	Response r = ctx.response();
//    	r.setHeader("Unauthorized", "401");
//    	r.setHeader("WWW-Authenticate", "Basic realm=\"Feedreader\"");
//    	return unauthorized("onUnauthorized");
//    }
}
