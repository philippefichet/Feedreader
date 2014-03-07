package controllers;

import org.apache.commons.codec.binary.Base64;

import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.mvc.Result;
import play.mvc.Security;

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
