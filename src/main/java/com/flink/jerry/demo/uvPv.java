package com.flink.jerry.demo;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Description TODO
 * @Auther wuzebin
 * @Date 2022/5/31 15:49
 */
public class uvPv {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();



        env.execute("currentDayUvPv");

    }

}
