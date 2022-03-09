package com.sanri.test.distributedid.multi;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * 使用数据库号段生成 id
 */
@Component
@Slf4j
public class DatabaseParagraph implements IdGenerator{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int queueSize = 1000;
    private static final int queryFloat = 100;
    private static final int realQueueSize = 1100;      // queueSize + queryFloat

    private BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(realQueueSize);

    private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(10));

    static final String querySql = "select id,max_id,step,biz_type,version from id_generator where biz_type = ? ";
    static final String queryCountSql = "select count(*) from id_generator where biz_type = ? ";
    static final String updateSql = "update id_generator set max_id = max_id + step ,version = version + 1 where id = ? and max_id = ? and version = ? ";

    /**
     * 号段模式需要初始化
     */
    public void init(){
        log.info("初始化号段表");
        final int [] bizs = {1000};

        for (int biz : bizs) {
            // 查询是否有此业务,如果没有,则初始化
            Integer count = jdbcTemplate.queryForObject(queryCountSql, new Object[]{biz}, Integer.class);
            if(count <= 0) {
                jdbcTemplate.update("insert into id_generator(max_id,step,biz_type,version) values (0,1000," + biz + ",0)");
            }
        }
    }

    RowMapperImpl rowMapper = new RowMapperImpl();

    @Override
    public String generateId(int bizType) {
        int size = blockingQueue.size();
        if(size <= queryFloat){
            executorService.submit(() -> {
                try {
                    // 如果数量少于 100 个, 从数据库获取号段
                    int update = 0;
                    ParagraphId paragraphId = null;
                    do {
                        paragraphId = jdbcTemplate.queryForObject(querySql, new Object[]{bizType}, rowMapper);
                        update = jdbcTemplate.update(updateSql, paragraphId.getId(), paragraphId.getMaxId(), paragraphId.getVersion());
                    } while (update != 1);

                    // 更新成功,拿到最新号段 (start,end]
                    Long start = paragraphId.getMaxId();
                    long end = start + paragraphId.getStep();

                    // 将号段生产进 blockQueue
                    while (++start != end) {
                        try {
                            // put 方法会在队列满的时候发生阻塞
                            blockingQueue.put(start);
                        } catch (InterruptedException e) {
                            log.error("put exception [{}]", e.getMessage(), e);
                        }
                    }
                }catch (Exception e){
                    log.error("号段生成可能出错了[{}]",e.getMessage(),e);
                }
            });
        }
        // 主线程继续从队列中拿号
        try {
         return  blockingQueue.take()+"";
        } catch (InterruptedException e) {
            log.error("拿唯一号异常 take {}",e.getMessage(),e);
        }
        return "";
    }

    class RowMapperImpl implements RowMapper<ParagraphId>{

        @Override
        public ParagraphId mapRow(ResultSet resultSet, int i) throws SQLException {
            ParagraphId build = ParagraphId.builder().id(resultSet.getInt(1))
                    .maxId(resultSet.getLong(2))
                    .step(resultSet.getInt(3))
                    .bizType(resultSet.getInt(4))
                    .version(resultSet.getInt(5)).build();
            return build;
        }
    }

    @Data
    @Builder
    static class ParagraphId{
        private Integer id;
        private Long maxId;
        private Integer step;
        private Integer bizType;
        private Integer version;

        @Tolerate
        public ParagraphId() {
        }
    }
}
