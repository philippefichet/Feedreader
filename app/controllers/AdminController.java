package controllers;

import java.util.HashMap;
import java.util.Map;

import conf.Application;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security;

public class AdminController extends Controller {
    
	@Security.Authenticated(SecuredAdmin.class)
    public static Result index() {
        return ok(views.html.feedEdit.render());
    }
    
	@Security.Authenticated(SecuredAdmin.class)
    public static Result application() {
		
        return ok(views.html.application.render(Application.getInstance(), new HashMap<String, String>()));
    }
	
	@Security.Authenticated(SecuredAdmin.class)
    public static Result saveApplication() {
		Map<String, String> errors = new HashMap<>();
		String[] bootswatch = request().body().asFormUrlEncoded().get("bootswatch");
		if (bootswatch != null && bootswatch.length > 0) {
			try {
				conf.WebJarsFiles.locate(bootswatch[0] + "/css/bootstrap.min.css");
				Application.getInstance().setBootswatch(bootswatch[0]);
			} catch(IllegalArgumentException e) {
				errors.put("bootswatch", e.getLocalizedMessage());
			}
			
		}
		
		return ok(views.html.application.render(Application.getInstance(), errors));
	}
}
