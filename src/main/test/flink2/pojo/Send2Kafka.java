package flink2.pojo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Random;

/**
 * 根据kafkaPojo的配置来模拟数据
 *
 * @Auther wuzebin
 * @Date 2022/5/23 12:17
 */
public class Send2Kafka extends Thread {


    Random random;
    KafkaPojo kafkaPojo;
    Producer<String, String> producer;
    long size;
    String[] uuidArr;
    int met;

    public Send2Kafka(KafkaPojo kafkaPojo,
                      Producer<String, String> producer,
                      long size,
                      String[] uuidArr,
                      int met){

        this.kafkaPojo=kafkaPojo;
        this.producer=producer;
        random = new Random();
        this.size=size;
        this.uuidArr=uuidArr;
        this.met=met;
    }


    @Override
    //线程体,启动线程时会运行run()方法中的代码
    public void run() {

        Integer metricsNum = kafkaPojo.getMetricsNum();
        String topic = kafkaPojo.getTopic();
        Integer number = kafkaPojo.getNumber();
        Integer eventStatusCode = kafkaPojo.getEventStatusCode();
        int i1,k1;String uuid;

        System.out.printf("send to %s\n参数：事件数:%s\t分类编号:%s\t事件状态数:%s\n",
                topic,
                metricsNum,
                number,
                eventStatusCode);

        int count = 0;
        while(count++ < size) {

            i1 = random.nextInt(metricsNum);
            k1 = random.nextInt(eventStatusCode+1);
            uuid = uuidArr[random.nextInt(met)];

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    ",,"+uuid+",,,,,,,,C1804111843,"+number+","+i1+",1652868489,"+k1+",1073923,40312,1652868489,1652868489");

            producer.send(record);
        }

        producer.close();
    }


}
