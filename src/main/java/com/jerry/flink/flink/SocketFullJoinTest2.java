package com.jerry.flink.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.joda.time.DateTime;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/25 16:33
 */
public class SocketFullJoinTest2 {
    public static void main(String[] args) {

        // 算子
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // table
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // table配置
        TableConfig config = tableEnv.getConfig();
        // 设置窗口内数据每隔3s触发一次
        // config.getConfiguration().setBoolean("table.exec.emit.early-fire.enabled", true);
        // config.getConfiguration().setString("table.exec.emit.early-fire.delay", "10s");

        DataStreamSource<String> socket1 = env.socketTextStream("127.0.0.1", 9000);
        DataStreamSource<String> socket2 = env.socketTextStream("127.0.0.1", 9001);
        DataStreamSource<String> socket3 = env.socketTextStream("127.0.0.1", 9002);

        Table socketTable1 = tableEnv.fromDataStream(socket1, $("f0"),
                $("proctime").proctime());
        Table socketTable2 = tableEnv.fromDataStream(socket2, $("f0"),
                $("proctime").proctime());
        Table socketTable3 = tableEnv.fromDataStream(socket3, $("f0"),
                $("proctime").proctime());

        tableEnv.createTemporaryView("socket1",socketTable1);
        tableEnv.createTemporaryView("socket2",socketTable2);
        tableEnv.createTemporaryView("socket3",socketTable3);

//        Table table1 = tableEnv.sqlQuery(
//                "SELECT f0,count(1),max(proctime) FROM socket1 group by f0 ,TUMBLE(proctime,INTERVAL '2' MINTUE)"
//        );
//        Table table2 = tableEnv.sqlQuery(
//                "SELECT f0,count(1),max(proctime) FROM socket1 group by f0 ,TUMBLE(proctime,INTERVAL '2' MINTUE)"
//        );



        tableEnv.executeSql(
                "select \n" +
//            "    if(t1.f0 is null,t2.f0,t1.f0),z1,z2\n" +
                        "    *\n" +
                        "from(\n" +
                        "    SELECT f0,count(1) z1 FROM socket1 group by f0 ,TUMBLE(proctime,INTERVAL '1' MINUTE)\n" +
                        ") t1 \n" +
                        "full outer join (\n" +
                        "    SELECT f0,count(1) z2 FROM socket2 group by f0 ,TUMBLE(proctime,INTERVAL '1' MINUTE)\n" +
                        ") t2\n" +
                        "on t1.f0=t2.f0 "
        ).print();







    }

    static class kafkaRichSinkFunction extends RichSinkFunction<Row> {

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
