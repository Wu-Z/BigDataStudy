package com.jerry.kafka.producer;

import java.util.Properties;
import java.util.Random;

import com.jerry.kafka.model.UserVisitWebEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @Description 测试自定义分区器
 * @Auther jerry
 * @Date 2022/3/8 15:59
 */
public class PartitionerProducer {

    private static final String[] PHONE_NUMS = new String[]{
            "10000", "10000", "11111", "13700000003", "13700000004",
            "10000", "15500000006", "11111", "15500000008",
            "17600000009", "10000", "17600000011"
    };

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "124.222.130.125:9092");
        // 设置分区器
        props.put("partitioner.class", "com.kafka.jerry.partitioner.PhonenumPartitioner");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.kafka.jerry.model.UserVisitWebEvent");

        Producer<String, UserVisitWebEvent> producer = new KafkaProducer<>(props);

        int count = 0;
        int length = PHONE_NUMS.length;

        while(count < 10) {
            Random rand = new Random();
            String phoneNum = PHONE_NUMS[rand.nextInt(length)];
            ProducerRecord<String, UserVisitWebEvent> record = new ProducerRecord<>("partition-test", phoneNum, UserVisitWebEvent
                    .builder()
                    .id("1")
                    .date("2022")
                    .pageId(2)
                    .userId("2")
                    .url("rr")
                    .build());
            RecordMetadata metadata = producer.send(record).get();
            String result = "phonenum [" + record.value() + "] has been sent to partition " + metadata.partition();
            System.out.println(result);
            Thread.sleep(500);
            count++;
        }
        producer.close();
    }
}
