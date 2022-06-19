package com.flink.jerry.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * markdown 类型钉钉消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDownMessage extends BaseMessage {

    public MarkDownContent markdown;

    public AtMobiles at;

    @Override
    protected void init() {
        this.msgtype = MessageType.markdown;
    }


    @Data
    public static class MarkDownContent {
        /**
         * 首屏会话透出的展示内容
         */
        private String title;

        /**
         * markdown格式的消息
         */
        private String text;
    }
}