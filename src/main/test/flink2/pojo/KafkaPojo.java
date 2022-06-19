package flink2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 *      kafka数据源的配置
 *
 *  {
 *       "id":1,						-- 数据源唯一标识符
 *       "type": "kafka",				 -- 数据源名称
 *       "conf": {						-- 数据源详细配置
 *         "groupid": "trace1",				 -- 消费组id
 *         "brokes":"172.18.212.86:9092,172.18.212.90:9092,172.18.212.96:9092",    -- 集群地址
 *         "topic": "AD_TRACE",		 -- topic的名字
 *         "fieldsLength": 10,			 -- 数据源字段长度
 *         "fields":{
 *             "0":[0,1,3],
 *             "2":[1,0,1],
 *             "3":[0,1,4],
 *             "sum(BIGINT&8)":[0,0,5],
 *             "9",[1,0,2],
 *             "count(*)":[0,0,6]
 *         }
 * --    对应解释：
 * --    "0":[0,1,1]
 * --        -> key("0")代表在原始数据的位置
 * --        -> [0,1,1]从左到右分别代表（是否为维度字段（1是0否））、（是否为关联字段（1是0否））以及在最终结果表的位置
 *       }
 *  }
 *
 *
 * @Auther wuzebin
 * @Date 2022/4/7 17:28
 */
@Data
@AllArgsConstructor
public class KafkaPojo implements Serializable  {

    // 顺序id
    private Integer id;
    // 分类编号
    private Integer number;
    // 消费组id
    private String groupid;
    // topic的IP地址
    private String brokers;
    // topic名称
    private String topic;
    // topic对应的数据长度
    private Integer fieldsLength;
    // 指标相关的字段数
    private Integer metricsNum;
    // 事件响应码的一个状态  0代表状态码只有一种  1代表有多种
    private Integer eventStatusCode;

}

