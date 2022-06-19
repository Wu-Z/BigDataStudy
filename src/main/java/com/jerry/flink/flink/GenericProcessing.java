package com.jerry.flink.flink;

import com.flink.jerry.pvuv.utils.UvExampleUtil;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/15 18:05
 */
public class GenericProcessing {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(TimeUnit.MINUTES.toMillis(1));
        env.setParallelism(2);

        CheckpointConfig checkpointConf = env.getCheckpointConfig();
        checkpointConf.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConf.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        Properties props1 = new Properties();
        props1.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, UvExampleUtil.broker_list);
        props1.put(ConsumerConfig.GROUP_ID_CONFIG, "test1");

        Properties props2 = new Properties();
        props2.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, UvExampleUtil.broker_list);
        props2.put(ConsumerConfig.GROUP_ID_CONFIG, "test2");

        FlinkKafkaConsumerBase<String> kafkaConsumer1 = new FlinkKafkaConsumer<>(
                "test1", new SimpleStringSchema(), props1)
                .setStartFromEarliest();

        FlinkKafkaConsumerBase<String> kafkaConsumer2 = new FlinkKafkaConsumer<>(
                "test2", new SimpleStringSchema(), props2)
                .setStartFromEarliest();


        SingleOutputStreamOperator<Tuple2> sum = env.addSource(kafkaConsumer1)
                .map(x -> {
                    String[] split = x.split(",");
                    return new Tuple2(split[0], 1);
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(x -> x.f0)
                .sum(1);

        SingleOutputStreamOperator<Tuple2> sum1 = env.addSource(kafkaConsumer2)
                .map(x -> {
                    String[] split = x.split(",");
                    return new Tuple2(split[0], 1);
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(x -> x.f0)
                .sum(1);

//        sum.join(sum1)
//                .where(x->x.f0)
//                .equalTo(x->x.f0)
//                .window(TumblingProcessingTimeWindows.of(Time.days(1)))
//                .apply((JoinFunction<Tuple2, Tuple2, String>) (first, second) -> first.f0+","+first.f1+","+second.f1)
//                .print()
//                ;

        sum.coGroup(sum1)
                .where(x->x.f0)
                .equalTo(x->x.f0)
                .window(TumblingProcessingTimeWindows.of(Time.days(1)))
                .apply((CoGroupFunction<Tuple2, Tuple2, String>) (first, second, out) -> {

                    if (first.iterator().hasNext()){
                        first.forEach(tupleF -> {
                            second.forEach(tupleS -> {
                                if (tupleF.f0==tupleS.f0) out.collect(tupleF.f0 +","+tupleF.f1+","+tupleS.f1);
                                else out.collect(tupleF.f0 +","+tupleF.f1+",");
                            });
                        });
                    }

                })
                .print();

        env.execute();

    }
}
