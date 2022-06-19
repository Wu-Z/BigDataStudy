package com.flink.jerry.demo;

import com.flink.jerry.config.Common;
import com.flink.jerry.util.StringUtil;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeutils.base.CharSerializer;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

/**
 * 基于Broadcast 状态的Flink Etl Demo
 * @Auther wuzebin
 * @Date 2022/5/30 18:16
 */
public class FlinkEtlDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "config",
                new SimpleStringSchema(),
                Common.getProperties()
        );

        String initPath = "D:\\esell\\code\\flink\\src\\main\\java\\com\\flink\\jerry\\demo\\cityCode.txt";
        DataStreamSource<String> initFile = env.readTextFile(initPath);
        DataStreamSource<String> randomCityString = env.addSource(new RandomFunction());

        MapStateDescriptor<String,String> descriptor = new MapStateDescriptor("dynamicConfig",
                CharSerializer.class,
                CharSerializer.class);

        DataStreamSource<String> dataStreamSource = env.addSource(consumer).setParallelism(2);

        BroadcastStream<String> configStream = dataStreamSource
                .union(initFile).broadcast(descriptor);

        SingleOutputStreamOperator<String> input = randomCityString.connect(configStream)
                .process(new BroadcastProcessFunction<String, String, String>() {
                    @Override
                    public void processBroadcastElement(String value,
                                                        BroadcastProcessFunction<String, String, String>.Context ctx,
                                                        Collector<String> out) throws Exception {

                        System.out.println("new config : " + value);
                        if (value.length()>3 && value.contains(",")){
                            // 更新广播状态
                            BroadcastState broadcastState = ctx.getBroadcastState(descriptor);
                            String[] split = value.split(",");
                            broadcastState.put(split[0],split[1]);
                        }

                    }

                    @Override
                    public void processElement(String value,
                                               BroadcastProcessFunction<String, String, String>.ReadOnlyContext ctx,
                                               Collector<String> out) throws Exception {

                        ReadOnlyBroadcastState<String,String> configMap = ctx.getBroadcastState(descriptor);
                        // 需要解析的code
                        String[] line = value.split(",");
                        String code = line[0];
                        String va = configMap.get(code);
                        if (va==null){
                            va = "中国(code="+code+")";
                        }else {
                            va = va + "(code="+code+")";
                        }

                        out.collect(va + "," + line[1]);
                    }

                });

        input.print();
        env.execute("BroadCastDemo");

    }
}



class RandomFunction implements SourceFunction<String> {

    Boolean flag = true;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (flag){

            for (int i = 0; i < 300; i++) {
                String nu = String.valueOf(i);
                while (nu.length() < 3) {
                    nu = "0" + nu;
                }
                ctx.collect(nu+","+ StringUtil.getRandomString(5));
                Thread.sleep(2000);
            }

        }
    }

    @Override
    public void cancel() {
        flag=false;
    }
}
