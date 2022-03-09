package com.sanri.test.testspringbootkafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.sanri.test.testspringbootkafka.producers.dto.EventSource;
import com.sanri.test.testspringbootkafka.producers.dto.ScreenEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public class SysTest {

    @Test
    public void test() throws ParseException {
        String timeformat = DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd'T'HH:mm:ss.S");
        Date parseDate = DateUtils.parseDate(timeformat, "yyyy-MM-dd'T'HH:mm:ss.S");
        System.out.println(parseDate);
    }

    @Test
    public void test3(){
        ScreenEvent screenEvent = new ScreenEvent();
//        screenEvent.setSource(new EventSource());
        screenEvent.setSource(EventSource.builder().sourceName("abc").build());
        String orElseGet = Optional.ofNullable(screenEvent.getSource()).flatMap(eventSource -> Optional.ofNullable(eventSource.getSourceName())).orElseGet(() -> "");
        Assert.assertNotNull(orElseGet);
    }

    class ${

    }
}
