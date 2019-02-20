package com.mermaid.framework.core.mvc.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 15:18
 * version 1.0
 */
@ConditionalOnExpression("true ")
@Controller
@ApiIgnore
public class MermaidFrameworkErrorController implements ErrorController{
    @Override
    public String getErrorPath() {
        return null;
    }
}
