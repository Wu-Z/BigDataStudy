package com.jerry.flink.trigger.triggers;

import com.jerry.flink.trigger.model.UserVisitWebEvent;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.concurrent.TimeUnit;

/**
 * @Description 时间或者计时器trigger
 * @Auther jerry
 * @Date 2022/3/4 11:48
 */
public class TimeCountTrigger extends Trigger<UserVisitWebEvent, TimeWindow> {

    private static int countAccumulate = 0;
    // 计数器
    private static int count = 0;
    //触发计算的最大数量
    private static int maxCount = 15;
    //定时触发间隔时长 (ms)
    private static Long interval = TimeUnit.MINUTES.toMillis(1);

    @Override
    public TriggerResult onElement(UserVisitWebEvent element
                                    , long timestamp
                                    , TimeWindow window
                                    , TriggerContext ctx) throws Exception {

        if (count < maxCount) {
            count++;
            countAccumulate++;
            return TriggerResult.CONTINUE;
        }else {
            System.out.println("当前数据总数：\t"+countAccumulate);
            System.out.println("触发计算\t count:\t"+count);
            count=0;
            return TriggerResult.FIRE;
        }

    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return null;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {

    }
}
