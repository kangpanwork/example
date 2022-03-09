package com.sanri.test.testmybatis;

import com.sanri.test.testmybatis.po.Dept;
import com.sanri.test.testmybatis.po.Emp;
import com.sanri.test.testmybatis.service.PropagationAService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestPropagation {

    @Autowired
    private PropagationAService propagationAService;

    @Test
    public void noTransCallTrans(){
        propagationAService.noTransCallTrans(provideDept(1234),provideEmps(1234,10));
    }

    @Test
    public void testInsertDept(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        List<Emp> emps = provideEmps(deptNo,5);

        propagationAService.insertDept(dept,emps);
    }

    @Test
    public void testInsertDeptWithTransaction(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        List<Emp> emps = provideEmps(deptNo,5);

        propagationAService.insertDeptTransaction(dept,emps);
    }

    @Test
    public void testInsertSteps(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        List<Emp> emps = provideEmps(deptNo,5);

        propagationAService.insertSteps(dept,emps);
    }

    @Test
    public void testTransactionParall(){
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        List<Emp> emps = provideEmps(deptNo,10);

        propagationAService.transactionParallel(dept,emps);
    }

    @Test
    public void testTransactionAsync() throws ExecutionException, InterruptedException {
        int deptNo = RandomUtils.nextInt(2, 5);

        Dept dept = provideDept(deptNo);

        List<Emp> emps = provideEmps(deptNo,10);

        propagationAService.transactionAsyncInner(dept,emps);
    }

    private List<Emp> provideEmps(int deptNo,int count) {
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < deptNo; i++) {
            Emp emp = new Emp();
            emp.setDeptNo(String.valueOf(deptNo));
            emp.setEname(RandomUtil.username());
            emp.setEmpNo(RandomUtils.nextInt(10,100));
            emp.setJob(RandomUtil.job());
            emp.setHiredate(RandomUtil.date());
            emp.setMgr(RandomUtils.nextInt(10,100));
            emp.setSal(RandomUtils.nextDouble(1000,100000));
            emp.setComm(RandomUtils.nextDouble(0,100));
            emps.add(emp);
        }
        return emps;
    }

    private Dept provideDept(int deptNo) {
        Dept dept = new Dept();
        dept.setDname(RandomUtil.username());
        dept.setLoc(RandomUtil.address());
        dept.setDeptNo(String.valueOf(deptNo));
        return dept;
    }
}
