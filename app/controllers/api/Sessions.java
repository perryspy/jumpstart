package controllers.api;

import play.mvc.Result;
import utils.SecurityUtil;

/**
 * Created with IntelliJ IDEA for jumpstart
 * User: phutchinson
 * Date: 3/18/13
 * Time: 9:24 PM
 */
public class Sessions extends ApiBaseController {

    public static Result create(){
        return ok();
    }

    public static Result delete(){
        SecurityUtil.clearSession();
        return ok();
    }
}
