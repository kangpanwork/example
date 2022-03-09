package com.sanri.test.testmybatis.service;

import com.sanri.test.testmybatis.mapper.EmpMapper;
import com.sanri.test.testmybatis.po.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

@Service
public class PropagationBService {
    @Autowired
    private EmpMapper empMapper;

    public void insertEmps(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void insertEmpsTransaction(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 支持当前事务，如果当前没有事务，则新建事务
     * 如果当前存在事务，则加入当前事务，合并成一个事务
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertEmpsTransactionRequired(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
        }
    }

    /**
     * 新建事务，如果当前存在事务，则把当前事务挂起
     * 这个方法会独立提交事务，不受调用者的事务影响，父级异常，它也是正常提交
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertEmpsTransactionRequiredNew(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
        }
    }

    /**
     * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则新建事务
     * 如果当前存在事务，它将会成为父级事务的一个子事务，方法结束后并没有提交，只有等父事务结束才提交
     * 如果它异常，父级可以捕获它的异常而不进行回滚，正常提交
     * 但如果父级异常，它必然回滚，这就是和 REQUIRES_NEW 的区别
     * @param emps
     */
    @Transactional(propagation = Propagation.NESTED)
    public void insertEmpsTransactionNested(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 如果当前存在事务，则加入事务
     * 如果当前不存在事务，则以非事务方式运行，这个和不写没区别
     * @param emps
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void insertEmpsTransactionSupport(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 以非事务方式运行
     * 如果当前存在事务，则把当前事务挂起
     * @param emps
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void insertEmpsTransactionNotSupport(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 如果当前存在事务，则运行在当前事务中
     * 如果当前无事务，则抛出异常，也即父级方法必须有事务
     * @param emps
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void insertEmpsTransactionMandatory(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    /**
     * 以非事务方式运行，如果当前存在事务，则抛出异常
     * @param emps
     */
    @Transactional(propagation = Propagation.NEVER)
    public void insertEmpsTransactionNever(List<Emp> emps){
        for (Emp emp : emps) {
            empMapper.insert(emp);
            if(true) throw new IllegalArgumentException("出 bug 了");
        }
    }

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));
    /**
     * 测试异步任务会不会导致事务回滚
     * @param emps
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertEmpsFuture(List<Emp> emps) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = () -> {
            int count = 0 ;
            for (Emp emp : emps) {
                empMapper.insert(emp);
                System.out.println("插入第 "+(++count) +" 条数据");
            }
            return count;
        };

        Future<Integer> future = threadPoolExecutor.submit(callable);
        future.get();
    }

    @Transactional
    public void insertEmpsFutureInner(List<Emp> emps) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = () -> {
            int count = 0 ;
            for (Emp emp : emps) {
                empMapper.insert(emp);
                System.out.println("插入第 "+(++count) +" 条数据");
                if(count == 6){
                    if(true) throw new IllegalArgumentException("出 bug 了");
                }
            }
            return count;
        };

        Future<Integer> future = threadPoolExecutor.submit(callable);
        future.get();
    }
}
