package com.sanri.test.jetcache.service;

import com.alicp.jetcache.CacheUtil;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.sanri.test.jetcache.utills.RandomDataService;
import com.sanri.test.jetcache.web.dtos.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TagService {
    // 模拟数据提供
    private RandomDataService randomDataService = new RandomDataService();

    /**
     * tags 数据查询
     * @return
     */
    @Cached(name = "sanri:tags",key = "targetObject.getTagKey(#page,#pageSize,#name)",expire = 15,timeUnit = TimeUnit.DAYS,cacheType = CacheType.REMOTE)
    public List<Tag> tags(long page, long pageSize,String name){
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tag tag = (Tag) randomDataService.populateData(Tag.class);
            tags.add(tag);
        }
        return tags;
    }

    public String getTagKey(long page, long pageSize,String name){
        log.info("获取到的 key 为 {}",page+"_"+pageSize+"_"+name);
        return page+"_"+pageSize+"_"+name;
    }

    /**
     * 只有特定条件下进行数据缓存
     * @param page
     * @param pageSize
     * @return
     */
    @Cached(name = "sanri:tags:condition",key = "T(com.sanri.test.jetcache.service.TagService.CacheCondition).key(#page,#pageSize)",
            expire = 15,timeUnit = TimeUnit.DAYS,cacheType = CacheType.REMOTE,
            condition = "T(com.sanri.test.jetcache.service.TagService.CacheCondition).condition(#page)"
    )
    public List<Tag> cacheCondition(int page,int pageSize){
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tag tag = (Tag) randomDataService.populateData(Tag.class);
            tags.add(tag);
        }
        return tags;
    }

    /**
     * 特定条件缓存 spel 方法
     */
    public static final class CacheCondition{
        /**
         * 缓存拼接动态 key
         * @param page
         * @param pageSize
         * @return
         */
        public static final String key(Integer page,Integer pageSize){
            return page+"_"+pageSize;
        }

        /**
         * 只有当 page = 1 的时候进行缓存
         * @param page
         * @return
         */
        public static final boolean condition(Integer page){
            return page == 1;
        }
    }
}
