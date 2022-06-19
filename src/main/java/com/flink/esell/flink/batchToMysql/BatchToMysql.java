package com.flink.esell.flink.batchToMysql;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.joda.time.DateTime;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/23 16:04
 */
public class BatchToMysql {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env);
        env.setParallelism(2);


        // 准备数据
//        ArrayList<Tuple2<Integer, Integer>> tuple3s = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0 ; i<10000 ; i++) {
//
//            int i1 = random.nextInt(100);
//            int i2 = random.nextInt(100);
//            Tuple2<Integer, Integer> objectObjectTuple2 = new Tuple2<>();
//            objectObjectTuple2.f0 = i1;
//            objectObjectTuple2.f1 = i2;
//
//            tuple3s.add(objectObjectTuple2);
//        }
//        DataStreamSource<Tuple2<Integer, Integer>> ds = env.fromCollection(tuple3s);

        DataStreamSource<String> vm = env.socketTextStream("127.0.0.1", 1001);
//        SingleOutputStreamOperator<Tuple2<String,String>> ds = vm.map(
//                new MapFunction<String, Tuple2<String, String>>() {
//                    @Override
//                    public Tuple2<String, String> map(String value) throws Exception {
//                        String[] split = value.split(",");
//                        return new Tuple2<>(split[0],split[1]);
//                    }
//                }
//        );


        // stream to table


        Table table1 = streamTableEnvironment.fromDataStream(
                vm,
                $("f0"),
                $("proctime").proctime()
        );


        streamTableEnvironment.createTemporaryView("test1", table1);

        Table table = streamTableEnvironment.sqlQuery(
                "select f0,count(1) " +
                        "from test1 " +
                        "group by f0," +
                        "TUMBLE(proctime,INTERVAL '30' SECOND)");
//        streamTableEnvironment.createTemporaryView("temp",table);
//        Table temp = streamTableEnvironment.sqlQuery("select * from temp");

        streamTableEnvironment.toDataStream(table)
                .addSink(new kafkaRichSinkFunction())
                .setParallelism(1)
                ;

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class kafkaRichSinkFunction extends RichSinkFunction<Row>{

        int count = 0;

        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("进入sink函数");
        }

        @Override
        public void close() throws Exception {
            System.out.println("结束");
        }

        /**
         * Writes the given value to the sink. This function is called for every record.
         *
         * <p>You have to override this method when implementing a {@code SinkFunction}, this is a
         * {@code default} method for backward compatibility with the old-style method only.
         *
         * @param value   The input record.
         * @param context Additional context about the input record.
         * @throws Exception This method may throw exceptions. Throwing an exception will cause the
         *                   operation to fail and may trigger recovery.
         */
        @Override
        public void invoke(Row value, Context context) throws Exception {
            String yyyyMMdd = new DateTime(System.currentTimeMillis()).toString("yyyyMMdd HHmmss");
            System.out.println("进入invoke"+count+value.toString()+"\t"+yyyyMMdd);
            if (count++!=10) ;
            else System.out.println(count);
        }
    }


}
