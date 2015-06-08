package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class MainController extends Controller {

    public Result index() {
        return ok(views.html.index.render("Hello from Java"));
    }

    public Result login() {
        return ok();
    }
}
