package com.mermaid.framework.mvc.error;

import com.mermaid.framework.mvc.APIResponse;
import com.mermaid.framework.mvc.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 16:01
 * version 1.0
 */
@ControllerAdvice
public class MermaidExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public APIResponse handle(Exception ex) {
        if(ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            APIResponse apiResponse = businessException.response();
            String[] errorMessageArguments = businessException.getErrorMessageArguments();
            StringBuilder stringBuilder = new StringBuilder();
            if(null != errorMessageArguments && errorMessageArguments.length > 0) {
                for (String errMsg : errorMessageArguments) {
                    stringBuilder.append(errMsg);
                }
            }
            apiResponse.setMessage(stringBuilder.toString());
            return apiResponse;
        } else {
            return APIResponse.fail(ex);
        }
    }
}
