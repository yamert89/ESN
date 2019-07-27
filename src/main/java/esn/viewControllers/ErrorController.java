package esn.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        System.out.println("Error");
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
            System.out.println(errorMsg);
            return errorPage;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Object[] getErrorCode(HttpServletRequest httpRequest) {
        Integer code = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        NestedServletException ex = (NestedServletException) httpRequest.getAttribute("javax.servlet.error.exception");
        return ex == null ? new Object[]{code} : new Object[]{code, ex.getCause().getMessage()};
    }
}
