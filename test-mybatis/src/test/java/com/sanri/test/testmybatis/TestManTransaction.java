package com.sanri.test.testmybatis;

import com.sanri.test.testmybatis.mapper.BatchMapper;
import com.sanri.test.testmybatis.po.Dept;
import com.sanri.test.testmybatis.po.Emp;
import com.sanri.test.testmybatis.service.ManTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestManTransaction {

    @Autowired
    private ManTransaction manTransaction;

    @Autowired
    private BatchMapper batchMapper;

    @Test
    public void testInsertDept(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        manTransaction.insertDept(dept);
    }

    @Test
    public void testInsertDeptSync(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        manTransaction.insertDeptSync(dept);
    }

    private Dept provideDept(int deptNo) {
        Dept dept = new Dept();
        dept.setDname(RandomUtil.username());
        dept.setLoc(RandomUtil.address());
        dept.setDeptNo(String.valueOf(deptNo));
        return dept;
    }

    @Test
    public void testMultiSqlTransaction(){
        // 这样不行,没事务,会出问题
//        batchMapper.testTransaction();

        batchMapper.testTransactionBegin();
    }
}
