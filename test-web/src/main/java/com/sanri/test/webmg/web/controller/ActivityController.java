package com.sanri.test.webmg.web.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sanri.test.webmg.mapper.ActActivityMapper;
import com.sanri.test.webmg.mapper.entity.ActActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appapi/activity")
public class ActivityController {

    @Autowired
    private ActActivityMapper actActivityMapper;

    @GetMapping("/db")
    public List fromdb(){
        LambdaQueryWrapper<ActActivity> wrapper =  new LambdaQueryWrapper();
        wrapper.eq(ActActivity::getType,"ASSIST");
        List list = actActivityMapper.selectList(wrapper);
        return list;
    }

    @GetMapping("/redis")
    public List fromRedis(){

        return null;
    }
}
