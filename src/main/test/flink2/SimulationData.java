package flink2;

import com.google.gson.Gson;
import flink2.pojo.KafkaPojo;
import flink2.pojo.Send2Kafka;
import flink2.pojo.ZkConfPojo;
import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * 多线程往5个topic发送模拟数据
 *
 * @Auther wuzebin
 * @Date 2022/5/23 12:17
 */
public class SimulationData {

    private static Properties props;

    static {
        props = new Properties();
//        props.put("bootstrap.servers","vm:9092");
        props.put("bootstrap.servers","192.168.62.99:9092");
        // 设置分区器
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public static void main(String[] args) {

        long size = 100L;
        int met = 200;

        Gson gson = new Gson();

        String[] uuidArr = initUUIDArr(met);
        ZkConfPojo zkConfPojo = gson.fromJson("{\n" +
                "  \"sourceList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"number\": 2001,\n" +
                "      \"groupid\": \"track_flink\",\n" +
                "      \"brokers\": \"127.0.0.1:9092\",\n" +
                "      \"topic\": \"YXF_TRACK\",\n" +
                "      \"fieldsLength\": 19,\n" +
                "      \"metricsNum\": 1,\n" +
                "      \"eventStatusCode\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"number\": 2002,\n" +
                "      \"groupid\": \"response_flink\",\n" +
                "      \"brokers\": \"127.0.0.1:9092\",\n" +
                "      \"topic\": \"YXF_ACTION\",\n" +
                "      \"fieldsLength\": 19,\n" +
                "      \"metricsNum\": 14,\n" +
                "      \"eventStatusCode\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"number\": 2003,\n" +
                "      \"groupid\": \"control_flink\",\n" +
                "      \"brokers\": \"127.0.0.1:9092\",\n" +
                "      \"topic\": \"YXF_CONTROL\",\n" +
                "      \"fieldsLength\": 19,\n" +
                "      \"metricsNum\": 33,\n" +
                "      \"eventStatusCode\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"number\": 2004,\n" +
                "      \"groupid\": \"activity_flink\",\n" +
                "      \"brokers\": \"127.0.0.1:9092\",\n" +
                "      \"topic\": \"YXF_ACTIVITY\",\n" +
                "      \"fieldsLength\": 19,\n" +
                "      \"metricsNum\": 7,\n" +
                "      \"eventStatusCode\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 4,\n" +
                "      \"number\": 2005,\n" +
                "      \"groupid\": \"notwork_flink\",\n" +
                "      \"brokers\": \"127.0.0.1:9092\",\n" +
                "      \"topic\": \"YXF_NOTWORK\",\n" +
                "      \"fieldsLength\": 19,\n" +
                "      \"metricsNum\": 10,\n" +
                "      \"eventStatusCode\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}\n", ZkConfPojo.class);
        List<KafkaPojo> sourceList = zkConfPojo.getSourceList();

        // 启动线程      发送的到kafka数据量 = size * 数据源数量
        sourceList.forEach(x->{
            Send2Kafka send2Kafka = new Send2Kafka(x,new KafkaProducer<>(props),size,uuidArr,met);
            send2Kafka.start();
        });

    }

    private static String[] initUUIDArr(int i) {

        String[] uuidArr = new String[i];

        for (int j = 0; j < i; j++) {
            uuidArr[j] = UUID.randomUUID().toString();
        }

        return uuidArr;
    }
}



/*
{
        "sourceList": [
        {
        "id": 0,
        "number": 2001,
        "groupid": "track_flink",
        "brokers": "124.222.130.125:9092",
        "topic": "YXF_TRACK",
        "fieldsLength": 19,
        "metricsNum": 1,
        "eventStatusCode": 0
        },
        {
        "id": 1,
        "number": 2002,
        "groupid": "response_flink",
        "brokers": "124.222.130.125:9092",
        "topic": "YXF_RESPONSE",
        "fieldsLength": 19,
        "metricsNum": 14,
        "eventStatusCode": 1
        },
        {
        "id": 2,
        "number": 2003,
        "groupid": "control_flink",
        "brokers": "124.222.130.125:9092",
        "topic": "YXF_CONTROL",
        "fieldsLength": 19,
        "metricsNum": 33,
        "eventStatusCode": 1
        },
        {
        "id": 3,
        "number": 2004,
        "groupid": "activity_flink",
        "brokers": "124.222.130.125:9092",
        "topic": "YXF_ACTIVITY",
        "fieldsLength": 19,
        "metricsNum": 7,
        "eventStatusCode": 1
        },
        {
        "id": 4,
        "number": 2005,
        "groupid": "notwork_flink",
        "brokers": "124.222.130.125:9092",
        "topic": "YXF_NOTWORK",
        "fieldsLength": 19,
        "metricsNum": 10,
        "eventStatusCode": 1
        }
}
*/
