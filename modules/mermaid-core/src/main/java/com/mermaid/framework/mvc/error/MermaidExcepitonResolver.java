package com.mermaid.framework.mvc.error;

import com.mermaid.framework.mvc.APIResponse;
import com.mermaid.framework.mvc.BusinessException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 15:22
 * version 1.0
 */
public class MermaidExcepitonResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            APIResponse apiResponse = businessException.response();
            apiResponse.setMessage(businessException.getMessage());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(apiResponse);
            return modelAndView;
        }
        return null;
    }
}
