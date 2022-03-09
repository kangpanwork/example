package com.sanri.test.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InfluxMain {
    @Test
    public void testfirst(){
        InfluxDB connect = InfluxDBFactory.connect("http://192.168.108.130:8086");
        try {
            // 允许批量如果满了 100 条数据,或者时间到了 2 秒就发送一个请求
            connect.setDatabase("abc").enableBatch(100,2000, TimeUnit.MILLISECONDS);
            // 保留策略
            connect.setRetentionPolicy("autogen");

            Query query = new Query("show databases");
            QueryResult queryResult = connect.query(query);
            String error = queryResult.getError();
            System.out.println("error: "+error);

            List<QueryResult.Result> results = queryResult.getResults();
            for (QueryResult.Result result : results) {
                List<QueryResult.Series> seriesList = result.getSeries();
                for (QueryResult.Series series : seriesList) {
                    System.out.println(series);
                }
            }
        } finally {
            connect.close();
        }
    }
}
