package com.mermaid.framework.core.cloud.configuration;

import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.interceptor.InvocationInterceptor;
import com.mermaid.framework.core.cloud.interceptor.InvocationInterceptorChain;
import feign.Contract;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.FeignFormatterRegistrar;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 22:29
 * version 1.0
 */
@Configuration
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConvertersObjectFactory;

    @Autowired(required = false)
    private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();

    @Autowired(required = false)
    private List<FeignFormatterRegistrar> feignFormatterRegistrars = new ArrayList<>();

    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(this.messageConvertersObjectFactory));
    }

    @Bean
    @ConditionalOnMissingBean
    public Encoder feignEncoder() {return new SpringEncoder(this.messageConvertersObjectFactory);}

    @Bean
    @ConditionalOnMissingBean
    public Contract feignContract() {
        return new SpringMvcContract(this.parameterProcessors,feignConversionService());
    }

    @Bean
    public FormattingConversionService feignConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        Iterator iterator = this.feignFormatterRegistrars.iterator();
        while (iterator.hasNext()) {
            FeignFormatterRegistrar feignFormatterRegistrar = (FeignFormatterRegistrar) iterator.next();
            feignFormatterRegistrar.registerFormatters(conversionService);
        }

        return conversionService;
    }

    @Bean
    public InvocationInterceptor requestTraceInvocationInterceptor(final ApplicationInfo applicationInfo) {
        return new InvocationInterceptor() {
            @Override
            public void intercept(String cloudServiceName, String url, RequestTemplate requestTemplate, InvocationInterceptorChain chain) {

            }
        };
    }
}
