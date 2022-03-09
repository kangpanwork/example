package com.sanri.test.juc;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import sun.java2d.SurfaceDataProxy;

import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * exchange 只适用于两个线程间的数据交换
 * 一个停留点交换一次对象
 */
public class ExchangeMain {

    ExecutorService executorService = Executors.newCachedThreadPool();

    Exchanger<ExchangeMoneyAndGoods> exchangeMoneyAndGoodsExchanger = new Exchanger<>();

    @Test
    public void testExchange() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        executorService.submit(() ->{
            ExchangeMoneyAndGoods exchangeMoneyAndGoods = new ExchangeMoneyAndGoods(new BigDecimal(1500), null);
            try {
                ExchangeMoneyAndGoods exchange = exchangeMoneyAndGoodsExchanger.exchange(exchangeMoneyAndGoods);
                exchangeMoneyAndGoods.setGoods(exchange.goods);
                exchangeMoneyAndGoods.setMoney(exchangeMoneyAndGoods.money.subtract(new BigDecimal(10)));

                System.out.println("买家得到商品 "+ exchangeMoneyAndGoods);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        Thread.sleep(1000);

        executorService.submit(() -> {
            ExchangeMoneyAndGoods exchangeMoneyAndGoods = new ExchangeMoneyAndGoods(new BigDecimal(10), "奶茶");
            try {
                ExchangeMoneyAndGoods exchange = exchangeMoneyAndGoodsExchanger.exchange(exchangeMoneyAndGoods);
                exchangeMoneyAndGoods.setGoods(null);
                exchangeMoneyAndGoods.setMoney(exchangeMoneyAndGoods.money.add(new BigDecimal(10)));

                System.out.println("卖家卖出商品 "+exchangeMoneyAndGoods);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }


    /**
     * 一手交钱一手交货,暂时不知道这个应用场景是什么
     */
    @Data
    @AllArgsConstructor
    static class ExchangeMoneyAndGoods{
        private BigDecimal money;
        private String goods;
    }
}
