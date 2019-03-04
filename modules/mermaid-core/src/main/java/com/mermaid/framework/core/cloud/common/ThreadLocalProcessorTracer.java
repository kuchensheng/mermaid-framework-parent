package com.mermaid.framework.core.cloud.common;

import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.util.GlobalUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 20:03
 */
public class ThreadLocalProcessorTracer {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalProcessorTracer.class);

    /**
     * 请求跟踪ID格式${服务名}-${实例ID}-${GUID}-|${stack}|:${order}
     */
    private static final Pattern TRACE_ID_PATTERN = Pattern.compile("(.*\\-\\d{4,5}\\-\\d{16})$|(.*\\-\\d{4,5}\\-\\d{16}):(\\d+)$|(.*\\-\\d{4,5}\\-\\d{16,26})\\-\\|(.*)\\|:(\\d+)$|(.*\\-\\d{4,5}\\-\\d{26})$");

    private static final ThreadLocal<ThreadLocalProcessorTracer> traceThreadLocal = new ThreadLocal<>();

    private static final int maxProcessorContextCount = 50;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String traceId;

    private Span spanInfo;

    private static ApplicationInfo applicationInfo = ApplicationInfo.getInstance();

    public ThreadLocalProcessorTracer() {
        this(createTraceId(),null);
    }



    public ThreadLocalProcessorTracer(String traceId,String parentSpanId) {
        this.spanInfo = new Span();
        this.spanInfo.setStartTime(System.currentTimeMillis());
        this.spanInfo.setHostPort(applicationInfo.getAppHost()+":"+applicationInfo.getAppPort());
        if(!StringUtils.hasText(parentSpanId)) {
            parentSpanId = traceId;
        }
        this.spanInfo.setParentSpanId(parentSpanId);
        this.spanInfo.setSpanName(applicationInfo.getAppName()+":"+applicationInfo.getAppPort());
        this.spanInfo.setTraceId(traceId);
        this.spanInfo.setSpanId(applicationInfo.getAppName()+"-"+applicationInfo.getAppId()+System.currentTimeMillis()+GlobalUIDGenerator.next());
        this.traceId = traceId;
    }


    private static String createTraceId() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(applicationInfo.getAppName())
            .append("-")
            .append(applicationInfo.getAppPort())
            .append(simpleDateFormat.format(new Date()))
            .append(GlobalUIDGenerator.next());
    return stringBuffer.toString();
    }

    public static ThreadLocalProcessorTracer get() {
        ThreadLocalProcessorTracer tracer = traceThreadLocal.get();
        if(null ==  tracer) {
            tracer = new ThreadLocalProcessorTracer();
            tracer.getSpanInfo().setParentSpanId(tracer.getTraceId());
            put(tracer);
        }
        return tracer;
    }

    public static ThreadLocalProcessorTracer get(String traceId) {
        ThreadLocalProcessorTracer tracer = traceThreadLocal.get();
        if(traceId.equals(tracer.traceId)) {
            return tracer;
        }else {
            tracer = new ThreadLocalProcessorTracer();
            tracer.traceId = traceId;
            put(tracer);
            return tracer;
        }

    }

    private static void put(ThreadLocalProcessorTracer tracer) {
        traceThreadLocal.set(tracer);
    }

    public void remove() {
        traceThreadLocal.remove();
    }
    public void stop() {
        long stop = System.currentTimeMillis();
        this.spanInfo.setEndTime(stop);
        this.spanInfo.setCostTime(stop - this.spanInfo.getStartTime());
    }

    public static void clean() {
        traceThreadLocal.remove();
    }

    public static ThreadLocalProcessorTracer getNextTracer(String traceId) {
        ThreadLocalProcessorTracer tracer = get(traceId);
        tracer.spanInfo.setParentSpanId(tracer.spanInfo.getSpanId());
        return tracer;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Span getSpanInfo() {
        return spanInfo;
    }

    public void setSpanInfo(Span spanInfo) {
        this.spanInfo = spanInfo;
    }
}
