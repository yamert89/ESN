package esn.viewControllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        try {

            ModelAndView errorPage = new ModelAndView("error");
            String errorMsg = "";
            int httpErrorCode = 0;
            Object[] error = null;
            try {
                error = getErrorCode(httpRequest);
                httpErrorCode = (int) error[0];
            } catch (Exception e) {
                e.printStackTrace();
                httpErrorCode = Integer.parseInt(httpRequest.getParameter("status"));
            }

            switch (httpErrorCode) {
                case 400: {
                    errorMsg = "Http Error Code: 400. Bad Request";
                    break;
                }
                case 401: {
                    errorMsg = "Http Error Code: 401. Unauthorized";
                    break;
                }
                case 403:{
                    if (error[2].equals("/auth")) errorMsg = "Вы уже авторизованы";
                    else errorMsg = "Http Error Code: 403. Ошибка доступа.";
                    break;
                }
                case 404: {
                    errorMsg = "Http Error Code: 404. Запрашиваемая страница не найдена";
                    break;
                }
                case 500: {
                    // errorMsg = "Ошибка на сервере"; //TODO replace

                    if (error.length > 1) errorMsg = (String) error[1];
                    if (errorMsg.contains("has already been invalidated")) {
                        errorPage.setViewName("auth");
                        return errorPage;
                    }
                    break;
                }
                default: {
                    errorMsg = "Неизвестная ошибка";
                }
            }
            errorPage.addObject("errorMsg", errorMsg);
            System.out.println("ERROR :  CODE: " + httpErrorCode + " |  URL: " + error[2] + " |  MESSAGE: " + errorMsg);
            return errorPage;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Object[] getErrorCode(HttpServletRequest httpRequest) throws Exception{
        Integer code = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        Optional<ServletException> ex = Optional.ofNullable((ServletException) httpRequest.getAttribute("javax.servlet.error.exception"));
        String message = ex.isPresent() ? ex.get().getMessage() : "";
        String errorUrl = (String) httpRequest.getAttribute("javax.servlet.error.request_uri");
        return new Object[]{code, message, errorUrl};
    }

    @PostMapping("/clienterror")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public void err(/*@RequestParam String event, @RequestParam String jqXHR,
                    @RequestParam String ajaxSettings, @RequestParam String thrownError*/ @RequestParam String error){
        System.out.println("CLIENT ERROR :  " + error);
    }
}
