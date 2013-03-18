package controllers.api;


import models.User;
import org.codehaus.jackson.JsonNode;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Controller;
import utils.SecurityUtil;

import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA for jumpstart
 * User: phutchinson
 * Date: 3/16/13
 * Time: 11:14 PM
 */
public class Users extends ApiBaseController {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create(){
        Form<User> userForm = form(User.class);
        userForm = userForm.bind(request().body().asJson());

        if(userForm.hasErrors()){
            return errorResult(userForm.errors());
        } else {
            User user = userForm.get();
            user.save();
            SecurityUtil.createAuthenticatedSession(user);
            return successfulSaveResult(user);
        }
    }
}
