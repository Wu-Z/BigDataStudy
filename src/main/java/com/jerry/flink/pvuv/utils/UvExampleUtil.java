package com.jerry.flink.pvuv.utils;

import com.google.gson.Gson;
import com.jerry.flink.trigger.model.UserVisitWebEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

/**
 * @desc 用于给统计 UV 的案例生成数据
 */
public class UvExampleUtil {
    public static final String broker_list = "127.0.0.1:9092";

    /**
     * kafka topic，Flink 程序中需要和这个统一
     */
    public static final String topic = "visit-log";

    public static final Random random = new Random();

    public static final Gson gson = new Gson();

    static Properties props = new Properties();
    static KafkaProducer producer = null;

    static {
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
    }


    public static void writeToKafka(Integer num) throws InterruptedException {
        DateTimeFormatter yyyyMMddFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 生成 0~9 的随机数做为 appId
        for(int i = 0; i<num; i++){
            String yyyyMMdd = yyyyMMddFormat.format(LocalDateTime.now());
            int pageId = random.nextInt(10);    // 随机生成页面 id
            int userId = random.nextInt(100);   // 随机生成用户 id

            UserVisitWebEvent userVisitWebEvent = UserVisitWebEvent.builder()
                    .id(UUID.randomUUID().toString())   // 日志的唯一 id
                    .date(yyyyMMdd)                     // 日期
                    .pageId(pageId)                     // 页面 id
                    .userId(Integer.toString(userId))   // 用户 id
                    .url("url/" + pageId)               // 页面的 url
                    .build();
            // 对象序列化为 JSON 发送到 Kafka
            ProducerRecord record = new ProducerRecord<String, String>(topic,
                    null, null, gson.toJson(userVisitWebEvent));
            producer.send(record);
            System.out.println("发送数据: " + gson.toJson(userVisitWebEvent));
        }
        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length!=2) System.err.println("参数异常  \n" +
                " 第一个参数为休眠时长（s）\n" +
                " 第二个参数为每次发送的条数");
        while (true) {
            Long s = Long.valueOf(args[0]);
            Integer num = Integer.valueOf(args[1]);
            writeToKafka(num);
            Thread.sleep(s*1000);
        }
    }
}
