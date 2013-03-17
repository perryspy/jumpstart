package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

/**
 * Main controller for the Application since this is a "one page app" that is using AngularJS for the client side MVC
 * architecture there really shouldn't be much in here.
 */
public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("test"));
    }
  
}
