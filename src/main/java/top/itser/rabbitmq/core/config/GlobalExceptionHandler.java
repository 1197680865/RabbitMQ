package top.itser.rabbitmq.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.itser.rabbitmq.core.result.CommonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author Zhangchen
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public CommonResult handle(HttpServletRequest request, Exception e){
        Map<String, Object> res = new HashMap<String, Object>();
        e.printStackTrace();
        logger.error(e.getMessage());

        if(e instanceof MethodArgumentNotValidException){
            res.put("eMessage","参数格式错误");
            return new CommonResult<>(400,e.getMessage(),res);
        }
        res.put("eMessage",e.getMessage());
        res.put("url",request.getRequestURL().toString());
        return new CommonResult<>(500,e.getMessage(),res);
    }

    @ExceptionHandler(value = MultipartException.class)
    public CommonResult handle(HttpServletRequest request, MultipartException e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return new CommonResult<>(500,e.getCause().toString(),e.getMessage());
    }
}
