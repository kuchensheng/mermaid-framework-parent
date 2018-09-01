package com.mermaid.framework.mvc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/9/1 22:21
 * version 1.0
 */
public class TracableRestTemplate extends RestTemplate implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(TracableRestTemplate.class);

    private Environment environment;

    public TracableRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
        this.registerTracableInterceptor(this.getInterceptors());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public TracableRestTemplate() {
        super();
        this.registerTracableInterceptor(this.getInterceptors());
    }

    private void registerTracableInterceptor(List<ClientHttpRequestInterceptor> interceptors) {
        interceptors.add(new PutTraceIds2HttpHeaderInterceptor());
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        return super.doExecute(url, method, requestCallback, responseExtractor);
    }

    @Override
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        if(!this.hasTraceInterceptor(interceptors)) {
            this.registerTracableInterceptor(interceptors);
        }
    }

    private boolean hasTraceInterceptor(List<ClientHttpRequestInterceptor> interceptors) {
        for(ClientHttpRequestInterceptor interceptor : interceptors) {
            if(interceptor instanceof PutTraceIds2HttpHeaderInterceptor) {
                return true;
            }
        }
        return false;
    }

    private class PutTraceIds2HttpHeaderInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            String currentTime = String.valueOf(System.currentTimeMillis());
            headers.add("currentReqTime",currentTime);


            String appName = environment.getProperty("spring.application.name");
            String port = environment.getProperty("spring.application.index");
            String traceId = appName+"-"+port+"-"+currentTime;
            headers.add("traceId",traceId);
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().add("traceId",traceId);
            return response;
        }
    }
}
