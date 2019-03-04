package com.mermaid.framework.socket;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-01 10:56
 * 描述：Socket服务接口
 */
public interface SocketServer {
    /**
     * 启动socket服务
     * @param port
     */
    void start(int port);

    /**
     * 停止服务器
     */
    void stop();

    SocketServerType getType();
}
