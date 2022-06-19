package flink2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 从zookeeper获取的json串
 *
 * @Auther wuzebin
 * @Date 2022/4/7 16:15
 */
@Data
@AllArgsConstructor
public class ZkConfPojo {

    // source端配置
    private List<KafkaPojo> sourceList;

}
