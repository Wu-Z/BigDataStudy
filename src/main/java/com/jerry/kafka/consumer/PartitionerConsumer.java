package com.jerry.kafka.consumer;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/8 16:57
 */

import com.jerry.kafka.model.UserVisitWebEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.util.Arrays;
import java.util.Properties;

public class PartitionerConsumer {
    public static void main(String[] args) {
        //加载Properties文件
        Properties props = new Properties();
        props.put("bootstrap.servers", "124.222.130.125:9092");
        // 设置分区器
        props.put("partitioner.class", "com.kafka.jerry.partition.PhonenumPartitioner");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.kafka.jerry.model.UserVisitWebEvent");
        props.put("group.id", "t1");
        //根据Properties创建kafka消费者对象
        KafkaConsumer<String, UserVisitWebEvent> consumer = new KafkaConsumer(props);
        //指定消费的topic
        consumer.subscribe(Arrays.asList("partition-test"));
        //消费数据
        while (true){
            ConsumerRecords<String, UserVisitWebEvent> poll = consumer.poll(100);
            for (ConsumerRecord<String, UserVisitWebEvent> records : poll) {
                System.out.println(records);
                System.out.println(records.value().toString());
            }
        }
    }
}