package com.jerry.flink.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import java.util.Random;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/14 10:40
 */
public class KafkaTestData {

    static String[] keys = new String[100];
    static Random rand = new Random();

    static {
        for (int i = 0; i < 100; i++) {
            String key = "key" + i;
            keys[i] = key;
        }
    }

    public static void main(String[] args) throws Exception {

//        test1  124.222.130.125:9092
        if (args.length==0) {
            System.err.println("缺少参数  ：  请输入tpoic,ip+端口号");
            return;
        }

        String topic = args[0];
        String ip = args[1];

        Properties props = new Properties();
        props.put("bootstrap.servers",ip);
        // 设置分区器
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Random random = new Random();
        int i1,k1;

        int count = 0;
        while(count++ < 1) {

            i1 = random.nextInt(33);
            k1 = random.nextInt(2);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    "testValue");
//            System.out.println("第"+count+"条:\t"+account_id);
            producer.send(record);
        }

        producer.close();
    }

}


