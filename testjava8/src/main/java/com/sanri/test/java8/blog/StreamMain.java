package com.sanri.test.java8.blog;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class StreamMain {
    static List<Vehicle> vehicles = new ArrayList<>();

    @Before
    public void init(){
        List<String> imeis = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            List<String> singleVehicleDevices = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                String imei = RandomStringUtils.randomAlphanumeric(15);
                singleVehicleDevices.add(imei);
            }
            imeis.add(StringUtils.join(singleVehicleDevices,','));
        }
        vehicles.add(new Vehicle("KPTSOA1K67P081452","17620411498","9420",1,4.5,imeis.get(0)));
        vehicles.add(new Vehicle("KPTCOB1K18P057071","15073030945","张玲",2,1.4,imeis.get(1)));
        vehicles.add(new Vehicle("KPTS0A1K87P080237","19645871598","sanri1993",1,3.0,imeis.get(2)));
        vehicles.add(new Vehicle("KNAJC526975740490","15879146974","李种",1,3.9,imeis.get(3)));
        vehicles.add(new Vehicle("KNAJC521395884849","13520184976","袁绍",2,4.9,imeis.get(4)));
    }

    public void forEach(){
        vehicles.forEach(vehicle -> System.out.println(vehicle));
    }

    @Test
    public void forEachMap(){
        Map<String,Integer> map = new HashMap<>();
        map.put("a",1);map.put("b",2);map.put("c",3);

        map.forEach((k,v) -> System.out.println("key:"+k+",value:"+v));
    }

    @Test
    public void filter(){
        List<Vehicle> collect = vehicles.stream().filter(vehicle -> vehicle.getScore() >= 3).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void vins(){
        List<String> vins = vehicles.stream().map(Vehicle::getVin).collect(Collectors.toList());
    }

    public void groupBy(){
        Map<Integer, Double> collect = vehicles.stream().collect(Collectors.groupingBy(Vehicle::getCompanyId, Collectors.summingDouble(Vehicle::getScore)));
        collect.forEach((k,v) -> System.out.println("companId:"+k+",sum:"+v));
    }

    @Test
    public void sortBy(){
//        vehicles.sort((v1,v2) -> v2.getScore().compareTo(v1.getScore()));

//        List<Vehicle> collect = vehicles.stream().sorted(Comparator.comparing(Vehicle::getScore).reversed()).collect(Collectors.toList());
//        collect.forEach(System.out::println);

        List<Vehicle> collect = vehicles.stream().sorted(Comparator.comparing(Vehicle::getScore).reversed()
                .thenComparing(Comparator.comparing(Vehicle::getCompanyId)))
                .collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    @Test
    public void flatMap() {
        // 示例一
        List<String> collect = vehicles.stream().map(vehicle -> {
            String deviceNos = vehicle.getDeviceNos();
            return StringUtils.split(deviceNos, ',');
        }).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    /**
     * 将 List 转成 Map ; key(vin) == > Vehicle
     */
    public void toMap(){
        Map<String, Vehicle> vinVehicles = vehicles.stream().collect(Collectors.toMap(Vehicle::getVin, vehicle -> vehicle));
    }

    @Test
    public void mapReduce() throws IOException {
        Double reduce = vehicles.stream().parallel().map(Vehicle::getScore).reduce(0d, Double::sum);
    }

    @Test
    public void percentage (){
        // 总的分值
        Double totalScore = vehicles.stream().parallel().map(Vehicle::getScore).reduce(0d, Double::sum);

        // 查看每一个司机占的分值比重
        List<String> collect = vehicles.stream()
                .mapToDouble(vehicle -> vehicle.getScore() / totalScore)
                .mapToLong(weight -> (long) (weight * 100))
                .mapToObj(percentage -> percentage + "%")
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 找出有没有姓李的司机
     */
    @Test
    public void testName(){
        boolean anyMatch = vehicles.stream().anyMatch(vehicle -> vehicle.getName().startsWith("李"));
        System.out.println(anyMatch);
    }

    /**
     * 检查所有司机的评分是否都大于 3 分
     */
    @Test
    public void testScore(){
        boolean allMatch = vehicles.stream().allMatch(vehicle -> vehicle.getScore() > 3);
        System.out.println(allMatch);
    }

    /**
     * 检查是否有 3 公司的特务
     */
    @Test
    public void testCompany(){
        boolean noneMatch = vehicles.stream().noneMatch(vehicle -> vehicle.getCompanyId() == 3);
        System.out.println(noneMatch);
    }
}
