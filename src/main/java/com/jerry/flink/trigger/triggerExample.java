package com.jerry.flink.trigger;


import com.flink.jerry.pvuv.utils.UvExampleUtil;
import com.jerry.flink.trigger.model.UserVisitWebEvent;
import com.google.gson.Gson;
import com.jerry.flink.trigger.triggers.TimeCountTrigger;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 *  jerry
 *  @Date 2022/3/4 10:49
 */
public class triggerExample {
    public static void main(String[] args) throws Exception {

        // 初始化环境：开启checkpoint以及设置并行度
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(TimeUnit.MINUTES.toMillis(1));
        env.setParallelism(2);

        // checkpoint 相关参数设置
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 设置检查点数据在取消或者任务失败情况下的保留策略
        // checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.);

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, UvExampleUtil.broker_list);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "app-trigger");

        FlinkKafkaConsumerBase<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                UvExampleUtil.topic
                , new SimpleStringSchema()
                , props)
                .setStartFromLatest()
                ;

        env.addSource(kafkaConsumer)
                .map(x-> new Gson().fromJson(x, UserVisitWebEvent.class))
                .keyBy(x->x.getDate())
                .timeWindow(Time.hours(1))
                .trigger(new TimeCountTrigger())
                .sum("pageId")
                .print()
        ;

        env.execute();



    }
}

















