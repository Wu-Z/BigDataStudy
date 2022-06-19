package com.jerry.flink.monitor.model;

import java.io.Serializable;

/**
 * 请求消息的抽象类
 */
public abstract class BaseMessage implements Serializable {

    public BaseMessage() {
        init();
    }

    /**
     * 消息类型
     */
    protected MessageType msgtype;


    public MessageType getMsgtype() {
        return msgtype;
    }

    /**
     * 初始化 MessageType 方法
     */
    protected abstract void init();
}