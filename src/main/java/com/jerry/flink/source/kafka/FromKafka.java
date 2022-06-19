package com.jerry.flink.source.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import java.util.Properties;

public class FromKafka {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        Configuration config = new Configuration();
        // 10s获取一次分区
        config.setString("connector.properties.flink.partition-discovery.interval-millis","10000");
        env.configure(config, Thread.currentThread().getContextClassLoader());


        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers","127.0.0.1:9092");
        kafkaProperties.setProperty("group.id","flink_2022_02_17");
        kafkaProperties.setProperty("flink.partition-discovery.interval-millis", "30000");

        FlinkKafkaConsumer<String> myConsumer  = new FlinkKafkaConsumer<>(
                "test",
                new SimpleStringSchema(),
                kafkaProperties
        );

        myConsumer.setStartFromEarliest();
        DataStreamSource<String> ds = env.addSource(myConsumer);

        ds.setParallelism(1)
                .print().setParallelism(1);


        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
}


