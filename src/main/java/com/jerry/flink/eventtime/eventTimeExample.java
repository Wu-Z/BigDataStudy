package com.jerry.flink.eventtime;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/24 14:21
 */
public class eventTimeExample {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        WatermarkStrategy<String> stringWatermarkStrategy = WatermarkStrategy
                .<String>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withIdleness(Duration.ofSeconds(3))
                .withTimestampAssigner(TimestampAssignerSupplier.of(
                        (SerializableTimestampAssigner<String>) (element, recordTimestamp)
                                -> Long.parseLong(element.split(",")[1]))
                );

        DataStreamSource<String> socket = env.socketTextStream("127.0.0.1", 9000);
        socket.assignTimestampsAndWatermarks(stringWatermarkStrategy)
                .map(x->
                        {
                            System.out.println(x);
                            return new Tuple2<>(x.split(",")[0], 1);
                        }
                )
                .returns(Types.TUPLE(Types.STRING,Types.INT))
                .keyBy(0)
                .timeWindow(Time.seconds(5))
                .sum(1)
                .print()
        ;

        env.execute();

    }

}
