package controllers.api;

import org.apache.http.protocol.ResponseServer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA for jumpstart
 * User: phutchinson
 * Date: 3/18/13
 * Time: 12:24 PM
 */
public class ApiBaseController extends Controller {

    protected static Result buildResult(Map<String, List<ValidationError>> errorMap){

        List<String> errorMessages = new ArrayList<String>();
        Map<String, Object> result = new HashMap<String, Object>();
        if(errorMap != null && !errorMap.isEmpty()){

            for (List<ValidationError> errorList : errorMap.values()) {
                for (ValidationError validationError : errorList) {
                    errorMessages.add(errorMessage(validationError));
                }
            }

            result.put("error", errorMessages);
        }

        return ok(Json.toJson(result));
    }


    public static String errorMessage(ValidationError error) {
        String printableKey = Messages.get(error.key());
        List<Object> args = new ArrayList<Object>();
        args.add(printableKey);
        args.addAll(error.arguments());

        return Messages.get(error.message(), args.toArray(new Object[]{args.size()}));
    }
}
