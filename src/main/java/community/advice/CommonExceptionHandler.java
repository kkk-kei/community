package community.advice;

import com.alibaba.fastjson.JSON;
import community.dto.ResultDTO;
import community.exception.CommonErrorCodeImp;
import community.exception.CommonException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  Throwable e, Model model) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            if(e instanceof CommonException){
                resultDTO = ResultDTO.errorOf((CommonException) e);
            }else {
                resultDTO = ResultDTO.errorOf(CommonErrorCodeImp.System_Error);
            }
            PrintWriter printWriter = null;
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                printWriter = response.getWriter();
                printWriter.write(JSON.toJSONString(resultDTO));
                printWriter.close();
            } catch (IOException ioException) {
            }
            return null;
        }else{
            if(e instanceof CommonException) model.addAttribute("errormsg",e.getMessage());
            else    model.addAttribute("errormsg","服务器歇逼了~");
            return new ModelAndView("error");
        }
    }

}
