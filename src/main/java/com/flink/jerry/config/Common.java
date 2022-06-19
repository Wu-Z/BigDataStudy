package com.flink.jerry.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.Properties;

/**
 *
 * @Auther jerry
 * @Date 2022/5/30 18:24
 */
public class Common {

    public static Properties getProperties(){

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, com.flink.jerry.pvuv.utils.UvExampleUtil.broker_list);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "flink");

        return properties;
    }

}
