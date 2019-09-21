package esn.viewControllers;

import esn.entities.Organization;
import esn.entities.User;
import esn.services.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

@Controller
public class ErrorController {
    private final static Logger logger = LogManager.getLogger(ErrorController.class);

    private EmailService emailService;

    @Autowired
    @Qualifier("adminEmailService")
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        logger.debug("err> uri: " + httpRequest.getRequestURI()
         + " | query: " + httpRequest.getQueryString()
         + "attrs :" );
        /*Enumeration<String> attributeNames = httpRequest.getAttributeNames();
        StringBuilder builder1 = new StringBuilder();
        while(attributeNames.hasMoreElements()) builder1.append(attributeNames.nextElement()).append(" | ");
        logger.debug(builder1.toString());*/

        try {


            String errorMsg = "";
            int httpErrorCode = 0;
            Object[] error = null;
            try {
                error = getErrorCode(httpRequest);
                httpErrorCode = (int) error[0];
            } catch (Exception e) {
                logger.debug("Альтернативное получение [status] ошибки");
                String par = httpRequest.getParameter("status");
                if (par != null) httpErrorCode = Integer.parseInt(par);
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
                    errorMsg = "Ошибка на сервере"; //TODO replace

                    if (error.length > 1) errorMsg = (String) error[1];
                    if (errorMsg.contains("has already been invalidated")) {
                        errorPage.setViewName("auth");
                        return errorPage;
                    }
                    break;
                }
                case 777: {
                    if (error.length > 1) errorMsg = error[1].toString();
                    else errorMsg = "неизвестная ошибка сервера";
                    break;
                }
                default: {
                    errorMsg = "Неизвестная ошибка";
                    /*Enumeration<String> attrs = httpRequest.getAttributeNames();
                    StringBuilder builder = new StringBuilder(errorMsg);
                    while (attrs.hasMoreElements()) {
                        String par = attrs.nextElement();
                        builder.append("\n").append(par).append("   :    ").append(httpRequest.getAttribute(par));
                    }
                    errorMsg = builder.toString();*/
                }
            }
            errorPage.addObject("errorMsg", errorMsg);

            logger.error("ERROR :  CODE: " + httpErrorCode + " |  URL: " + error[2] == null ? "empty" : error[2] + " |  MESSAGE: " + errorMsg);
            return errorPage;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            errorPage.addObject("errorMsg", "Ошибка обработчика ERROR CONTROLLER"); //TODo rename
            return errorPage;
        }

    }

    private Object[] getErrorCode(HttpServletRequest httpRequest) throws Exception{
        Integer code = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        Optional<ServletException> ex = Optional.ofNullable((ServletException) httpRequest.getAttribute("javax.servlet.error.exception"));
        Map<String, String> controllerErrorMap = (Map<String, String>)httpRequest.getAttribute("org.springframework.web.servlet.DispatcherServlet.INPUT_FLASH_MAP");
        String message = ex.isPresent() ? ex.get().getMessage() : controllerErrorMap == null ? "" :
                controllerErrorMap.getOrDefault("flash", "Неизвестная ошибка контроллера");

        String errorUrl = (String) httpRequest.getAttribute("javax.servlet.error.request_uri");
        return new Object[]{code, message, errorUrl};
    }

    @PostMapping("/clienterror")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public void err(@RequestParam String error, HttpSession session){
        try {
            logger.error("CLIENT ERROR :  " + error);
            User user = (User) session.getAttribute("user");
            Organization org = (Organization) session.getAttribute("org");
            emailService.send("CLIENT ERROR", error,
                    "User: " + user + "\n org: " + org.getUrlName(), null);
        }catch (Exception e){logger.error("client error handler exc", e);}

    }
}
