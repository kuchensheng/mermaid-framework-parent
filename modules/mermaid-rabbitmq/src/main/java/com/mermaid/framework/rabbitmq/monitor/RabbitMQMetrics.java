package com.mermaid.framework.rabbitmq.monitor;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * rabbitMq监控类
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 9:27
 */
public class RabbitMQMetrics {
    // 发送计时
    private Timer sendTimer;
    // 消费计时
    private Timer consumeTimer;
    // 监控类名
    private String className;
    // 发送成功数
    private Counter sendSuccessCounter;
    //　发送失败数
    private Counter sendFailCounter;
    // 发送成功率
    private Gauge<Double> sendSuccessRate;

    final MetricRegistry metricRegistry = new MetricRegistry();

    public RabbitMQMetrics() {
    }

    public RabbitMQMetrics(String className) {
        this.className = className;
        init();
    }

    private void init() {
        sendTimer = metricRegistry.timer(MetricRegistry.name(className, "sendTime"));
        consumeTimer = metricRegistry.timer(MetricRegistry.name(className, "cosumeTime"));
        sendSuccessCounter = metricRegistry.counter(MetricRegistry.name(className, "sendSuccessCount"));
        sendFailCounter = metricRegistry.counter(MetricRegistry.name(className, "sendFailCount"));
        sendSuccessRate = new Gauge<Double>() {
            @Override
            public Double getValue() {
                if(sendSuccessCounter.getCount() == 0  && sendFailCounter.getCount() == 0) {
                    return 1D;
                }
                return (Double.valueOf(sendSuccessCounter.getCount())/(sendSuccessCounter.getCount()+sendFailCounter.getCount()));
            }
        };
        metricRegistry.register(MetricRegistry.name(className,"sendSuccessRate","rate"),sendSuccessRate);
    }

    /**
     * 发送时间计时器
     * @return
     */
    public Timer.Context startSendTiming() {
        return sendTimer.time();
    }

    /**
     * 消费时间计时器
     * @return
     */
    public Timer.Context startConsumeTiming() {
        return consumeTimer.time();
    }

    /**
     * 发送成功数递增
     */
    public void incSendSuccessCount() {
        sendSuccessCounter.inc();
    }

    /**
     * 发送失败数量递增
     */
    public void incSendFailCount() {
        sendFailCounter.inc();
    }


    public Timer getSendTimer() {
        return sendTimer;
    }

    public void setSendTimer(Timer sendTimer) {
        this.sendTimer = sendTimer;
    }

    public Timer getConsumeTimer() {
        return consumeTimer;
    }

    public void setConsumeTimer(Timer consumeTimer) {
        this.consumeTimer = consumeTimer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Counter getSendSuccessCounter() {
        return sendSuccessCounter;
    }

    public void setSendSuccessCounter(Counter sendSuccessCounter) {
        this.sendSuccessCounter = sendSuccessCounter;
    }

    public Counter getSendFailCounter() {
        return sendFailCounter;
    }

    public void setSendFailCounter(Counter sendFailCounter) {
        this.sendFailCounter = sendFailCounter;
    }

    public Gauge<Double> getSendSuccessRate() {
        return sendSuccessRate;
    }

    public void setSendSuccessRate(Gauge<Double> sendSuccessRate) {
        this.sendSuccessRate = sendSuccessRate;
    }

    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}
