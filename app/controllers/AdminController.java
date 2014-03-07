package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class AdminController extends Controller {
    
	@Security.Authenticated(SecuredAdmin.class)
    public static Result index() {
        return ok(views.html.feedEdit.render());
    }
    
}
