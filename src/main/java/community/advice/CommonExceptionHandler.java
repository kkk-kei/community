package community.advice;

import community.exception.CommonException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(Throwable e,Model model) {
//        HttpStatus status = getStatus(request);
        if(e instanceof CommonException) model.addAttribute("errormsg",e.getMessage());
        else    model.addAttribute("errormsg","服务器歇逼了~");
        return new ModelAndView("error");
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        HttpStatus status = HttpStatus.resolve(code);
//        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
//    }

}
