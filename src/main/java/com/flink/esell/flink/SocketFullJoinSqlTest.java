package com.flink.esell.flink;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.joda.time.DateTime;
import redis.clients.jedis.Tuple;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

//  机子 vm
//  mysql     @33#12opd155..dDsd)ds

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/25 9:50
 */
public class SocketFullJoinSqlTest {

    public static void main(String[] args) throws Exception {

        // 算子
        StreamExecutionEnvironment env
                = StreamExecutionEnvironment.getExecutionEnvironment();
        // table
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 状态ttl时间
        tableEnv.getConfig().setIdleStateRetention(Duration.ofSeconds(20));

        // 数据源配置
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

        Table table1 = tableEnv.sqlQuery("SELECT f0,count(1) z1 FROM socket1 group by f0 ,TUMBLE(proctime,INTERVAL '20' SECOND)");
        Table table2 = tableEnv.sqlQuery("SELECT f0,count(1) z2 FROM socket2 group by f0 ,TUMBLE(proctime,INTERVAL '20' SECOND)");
        Table table3 = tableEnv.sqlQuery("SELECT f0,count(1) z3 FROM socket3 group by f0 ,TUMBLE(proctime,INTERVAL '20' SECOND)");

        tableEnv.createTemporaryView("table1",table1);
        tableEnv.createTemporaryView("table2",table2);
        tableEnv.createTemporaryView("table3",table3);

        // 会存储中间状态
        Table temp = tableEnv.sqlQuery("select if(t1.f0 is null,t2.f0,t1.f0) as f0 ," +
                " if(z1 is null,0,z1) z1, " +
                " if(z2 is null,0,z2) z2 " +
                "from table1 t1 full join table2 t2 on t1.f0=t2.f0 ");

        tableEnv.createTemporaryView("temp1",temp);

        tableEnv.executeSql("select if(t1.f0 is null,t2.f0,t1.f0)," +
                " if(z1 is null,0,z1) z1, " +
                " if(z2 is null,0,z2) z2, " +
                " if(z3 is null,0,z3) z3 " +
                "from temp1 t1 full join table3 t2 on t1.f0=t2.f0 ")
                .print();

        // 中间数据测试
//        tableEnv.toDataStream(table1).print();
//        tableEnv.toDataStream(table2).print();


        env.execute();


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
