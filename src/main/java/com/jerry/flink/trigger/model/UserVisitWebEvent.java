package com.jerry.flink.trigger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVisitWebEvent implements Serializable {

    /**
     * 日志的唯一 id
     */
    private String id;

    /**
     * 日期，如：20191025
     */
    private String date;

    /**
     * 页面 id
     */
    private Integer pageId;

    /**
     *  用户的唯一标识，用户 id
     */
    private String userId;

    /**
     * 页面的 url
     */
    private String url;

}