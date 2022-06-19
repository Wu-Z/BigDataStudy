package com.jerry.flink.monitor.model;

import lombok.Data;
import java.util.List;


@Data
public class AtMobiles {
    /**
     * 被@人的手机号
     *
     * @return
     */
    public List<String> atMobiles;

    /**
     * @所有人时:true,否则为:false
     */
    public Boolean isAtAll;
}