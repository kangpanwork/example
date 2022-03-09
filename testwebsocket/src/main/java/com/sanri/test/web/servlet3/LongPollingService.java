package com.sanri.test.web.servlet3;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Service
public class LongPollingService implements ApplicationListener<ConfigChangeEvent>{
    ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    public void newLongPollClient(HttpServletRequest request){
        // 一定要由HTTP线程调用，否则离开后容器会立即发送响应
        final AsyncContext asyncContext = request.startAsync();
        // AsyncContext.setTimeout()的超时时间不准，所以只能自己控制
        asyncContext.setTimeout(0L);

        // 提交一个任务，添加一个新的长轮询客户端
        schedule.execute(new ClientLongPolling(asyncContext));
    }

    private Queue<ClientLongPolling> subs = new ConcurrentLinkedDeque<ClientLongPolling>();

    class ClientLongPolling implements Runnable{
        Future<?> asyncTimeoutFuture;
        AsyncContext asyncContext;

        public ClientLongPolling(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        void sendResponse(List<String> changedGroups) {
            /**
             *  取消超时任务
             */
            if (null != asyncTimeoutFuture) {
                asyncTimeoutFuture.cancel(false);
            }
            generateResponse(changedGroups);
        }

        void generateResponse(List<String> changedGroups) {
            if (null == changedGroups) {
                /**
                 * 告诉容器发送HTTP响应
                 */
                asyncContext.complete();
                return;
            }

            HttpServletResponse response = (HttpServletResponse)asyncContext.getResponse();

            try {

                // 禁用缓存
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(JSON.toJSONString(changedGroups));
                asyncContext.complete();
            } catch (Exception se) {
                asyncContext.complete();
            }
        }

        @Override
        public void run() {
            asyncTimeoutFuture = schedule.schedule(() -> {
                // 到点了， 移除自己
                subs.remove(this);

                // 给客户端发送响应，再次检查是否有变更，这里默认没有
                sendResponse(null);
            }, (long) 29.5, TimeUnit.SECONDS);

            subs.add(this);
        }
    }

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        String dataId = event.getDataId();
        String groupId = event.getGroupId();
        Iterator<ClientLongPolling> iterator = subs.iterator();
        while (iterator.hasNext()){
            ClientLongPolling clientLongPolling = iterator.next();
            // 这里应该比较客户端的 md5 和 这个变更后的 md5 ，这里默认有变更
            clientLongPolling.sendResponse(Collections.singletonList(groupId));
        }
    }
}
