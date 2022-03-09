package com.sanri.test.jetcache.web.controller;

import com.sanri.test.jetcache.service.TagService;
import com.sanri.test.jetcache.web.dtos.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JetCacheController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public List<Tag> tagList(long page, long pageSize,String name){
        return tagService.tags(page,pageSize,name);
    }

    @GetMapping("/tag/condition")
    public List<Tag> tagListConditionCache(int page, int pageSize){
       return tagService.cacheCondition(page,pageSize);
    }
}
