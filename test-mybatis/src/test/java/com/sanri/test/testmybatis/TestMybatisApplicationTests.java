package com.sanri.test.testmybatis;

import com.sanri.test.testmybatis.enums.BatchStatus;
import com.sanri.test.testmybatis.mapper.BatchMapper;
import com.sanri.test.testmybatis.mapper.EmpMapper;
import com.sanri.test.testmybatis.po.Batch;
import com.sanri.test.testmybatis.po.DeptEmps;
import com.sanri.test.testmybatis.po.Emp;
import com.sanri.test.testmybatis.po.EmpDept;
import com.sanri.test.testmybatis.utils.ListSplitIterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.session.ResultHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestMybatisApplicationTests {

    @Autowired
    EmpMapper empMapper;

    @Autowired
    BatchMapper batchMapper;

    @Test
    public void contextLoads() {
//        List<Emp> emps = empMapper.selectAll();
//       System.err.println(emps);

        List<Batch> batches = batchMapper.selectAll();
       System.err.println(batches.size());
    }

    @Test
    public void testDateParam() throws ParseException {
        Date date = DateUtils.parseDate("1983-01-12", "yyyy-MM-dd");
        List<Emp> emps = empMapper.dateParam(date);
        System.out.println(emps);
    }

    @Test
    public void testEntityParam(){
        Emp emp = new Emp();
        emp.setEname("A");

        List<Emp> emps = empMapper.selectEmpsUseParam(emp);
        System.out.println(emps.size());
    }

    @Test
    public void testMapParam(){
        Map<String,Object> map = new HashMap<>();
        map.put("ename","A");

        List<Emp> emps = empMapper.selectEmpsUseMap(map);
        System.out.println(emps.size());
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void testTypeHandler(){
        Batch batch = new Batch();
        batch.setBirthday(new Date());
        batch.setName("sanri");
        batch.setSuccess(true);
        batch.setSal(22.25);
        batch.setStatus(BatchStatus.PUBLISH);
        batchMapper.insert(batch);
    }

    @Test
    public void testQueryTimeInterceptor(){
        Example example = new Example(Batch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("phone","176%");
        List<Batch> batches = batchMapper.selectByExample(example);
       System.err.println(batches.size());
    }

    @Test
    public void testOne2One(){
        EmpDept smith = empMapper.selectOne2One("SMITH");
       System.err.println(smith);
    }

    @Test
    public void testOne2Multi(){
        DeptEmps deptEmps = empMapper.selectOne2Multi("20");
       System.err.println(deptEmps);
    }
    
    @Test
    public void testEmpsQuery(){
        List<Emp> emps = empMapper.selectEmpsByDeptNo("20");
       System.err.println(emps);
    }

    @Test
    public void testOne2MultiLazy(){
        DeptEmps deptEmps = empMapper.selectOne2MultiLazy("20");
        System.out.println(deptEmps.getEmps());
        System.out.println();
    }

    @Test
    public void testInsertList(){
        List<Batch> batches = new ArrayList<>();
        String [] batchStatues = {"publish","recall","active"};

        for (int i = 0; i < 10; i++) {
            String name = RandomUtil.username();
            String job = RandomUtil.job();
            String email = RandomUtil.email(40);
            String phone = RandomUtil.phone();
            String idcard = RandomUtil.idcard();
            String address = RandomUtil.address();
            Date date = RandomUtil.date();
            double sal = RandomUtils.nextDouble(1000,10000);
            boolean success = RandomUtils.nextBoolean();
            BatchStatus batchStatus = BatchStatus.parser(batchStatues[RandomUtils.nextInt(0,3)]);
            batches.add(new Batch(date,address,phone,success,idcard,name,job,email,sal,batchStatus));
        }
        batchMapper.insertList(batches);
    }

    /**
     * ?????? 10 ?????????,?????? insertList ???sql ???
     * insert into xxxx values
     * (...),(...)
     */
    @Test
    public void testInsert10wData(){
        String [] batchStatues = {"publish","recall","active"};
        List<Batch> batches = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int i = 1; i <= 100000; i++) {
            if(i % 100 == 0){
                //?????????????????????????????????
                batchMapper.insertList(batches);
                //????????????
                log.info("???????????????["+i+"]???,??????:"+NumberUtil.percent(i,100000,2) + " %,??????:"+stopWatch.getTime()+" ms");
                batches = new ArrayList<>();
            }
            String name = RandomUtil.username();
            String job = RandomUtil.job();
            String email = RandomUtil.email(40);
            String phone = RandomUtil.phone();
            String idcard = RandomUtil.idcard();
            String address = RandomUtil.address();
            Date date = RandomUtil.date();
            double sal = RandomUtils.nextDouble(1000,10000);
            boolean success = RandomUtils.nextBoolean();
            BatchStatus batchStatus = BatchStatus.parser(batchStatues[RandomUtils.nextInt(0,3)]);

            Batch batch = new Batch(date,address,phone,success,idcard,name,job,email,sal,batchStatus);
            batches.add(batch);
        }

        stopWatch.stop();
        log.info("????????????:"+stopWatch.getTime());
    }

    /**
     * ???????????????????????????,????????????(3 ???) : 217 ms
     */
    @Test
    public void test10wPhoneSpendTime(){
        List<Batch> batches = batchMapper.selectAll();
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (Batch batch : batches) {
            RandomUtil.phone();
        }
        stopWatch.stop();
        log.info("?????? "+batches.size() + " ???????????????:"+stopWatch.getTime()+" ms");
    }

    /**
     * ?????? insert into xxx values
     * (...),(...)
     * duplicate key update
     * xx=values('xxxx')
     *???????????????
     *
     * ?????? 100009 ?????? ???16G ??????,I5-6500 cpu  3.2G hz
     * 10   ?????????????????????330427 ms
     * 100  ?????????????????????99699 ms ??????
     * 500  ?????????????????????334591 ms
     * 1000 ?????????????????????629403 ms
     */
    @Test
    public void testBatchUpdateUseInsert(){
        //?????????????????? 10w ???????????????????????????????????? 5000 ???; ?????????????????????????????????
        List<Batch> batches = batchMapper.selectAll();

        log.info("????????? ???"+batches.size()+" ????????????...");
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        int batchCount = 500;
        ListSplitIterator<Batch> batchListSplitIterator = new ListSplitIterator<>(batches, batchCount);
        while (batchListSplitIterator.hasNext()){
            List<Batch> batchSubList = batchListSplitIterator.next();
            for (Batch batch : batchSubList) {
                batch.setPhone(RandomUtil.phone());
            }
            batchMapper.batchUpdateUseInsert(batchSubList);
            long position = batchListSplitIterator.position();
            log.info("????????????:"+position+",?????????:"+NumberUtil.percent(position,batches.size(),2)+",??????:"+stopWatch.getTime()+" ms");
        }
        stopWatch.stop();
        log.info("?????????????????????:"+stopWatch.getTime() +" ms");
    }

    /**
     * ?????????????????????
     * 500 ????????? 37260 ms
     * 1000 ????????? 35180 ms ??????
     * 1500  ????????? 39125 ms
     * 2000 ????????? 44772 ms
     */
    @Test
    public void testUpdateUseTmpTable(){
        //?????????????????? 10w ???????????????????????????????????? 5000 ???; ?????????????????????????????????
        List<Batch> batches = batchMapper.selectAll();

        log.info("????????? ???"+batches.size()+" ????????????...");
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        int batchCount = 1500;
        ListSplitIterator<Batch> batchListSplitIterator = new ListSplitIterator<>(batches, batchCount);
        while (batchListSplitIterator.hasNext()){
            List<Batch> batchSubList = batchListSplitIterator.next();
            for (Batch batch : batchSubList) {
                batch.setPhone(RandomUtil.phone());
            }
            batchMapper.batchUpdateUseTmpTable(batchSubList,System.currentTimeMillis());
            long position = batchListSplitIterator.position();
            log.info("????????????:"+position+",?????????:"+NumberUtil.percent(position,batches.size(),2)+",??????:"+stopWatch.getTime()+" ms");
        }
        stopWatch.stop();
        log.info("?????????????????????:"+stopWatch.getTime() +" ms");
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private List<DataSource> dataSources;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void manTransaction(){
        Batch result = transactionTemplate.execute(status -> {
            String sql = "";
            Batch batch = batchMapper.selectByPrimaryKey(23);
            return batch;
        });
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    public void testInsert(){
        DataSource dataSource = dataSources.get(0);

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try{
            String sql = "update dept set dname='mlzdsf' where deptno = 3 ";
            Connection connection = DataSourceUtils.getConnection(dataSource);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int updateCount = preparedStatement.executeUpdate();
            System.out.println(updateCount);

            if (false){
                throw new IllegalArgumentException("???????????? bug ");
            }
            transactionManager.commit(transaction);
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(transaction);
        }
    }
}
