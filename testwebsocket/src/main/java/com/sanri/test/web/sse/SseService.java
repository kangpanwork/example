package com.sanri.test.web.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
@Slf4j
public class SseService {
    /**
     * 保存所有的客户端
     */
    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    /**
     * 新的客户端
     * @param sseEmitter
     * @param id
     * @return
     */
    public SseEmitter accept(String id){
        SseEmitter sseEmitter = new SseEmitter(3600_000L);
        sseCache.put(id,sseEmitter);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteAddr = request.getRemoteAddr();

        sseEmitter.onError(new Error(id,remoteAddr));
        sseEmitter.onTimeout(new Timeout(id,remoteAddr));
        sseEmitter.onCompletion(new Completion(id,remoteAddr));
        return sseEmitter;
    }

    /**
     * 向前端推送数据
     * @param id
     * @param content
     */
    public void send(String id, String content) throws IOException {
        SseEmitter sseEmitter = sseCache.get(id);
        if(sseEmitter != null){
            sseEmitter.send(content);
        }
    }

    /**
     * 数据发送完成
     * @param id
     */
    public void complete(String id){
        SseEmitter sseEmitter = sseCache.get(id);
        if(sseEmitter != null){
            sseEmitter.complete();
        }
    }

    static class Completion implements Runnable{
        private String id;
        private String remoteAddr;

        public Completion(String id, String remoteAddr) {
            this.id = id;
            this.remoteAddr = remoteAddr;
        }

        @Override
        public void run() {
            log.error("客户端完成,id:[{}],remote:[{}],[{}]",id,remoteAddr);

            // 移除客户端
            sseCache.remove(id);
        }
    }

    static class Timeout implements Runnable{
        private String id;
        private String remoteAddr;

        public Timeout(String id, String remoteAddr) {
            this.id = id;
            this.remoteAddr = remoteAddr;
        }

        @Override
        public void run() {
            log.error("客户端超时,id:[{}],remote:[{}],[{}]",id,remoteAddr);
        }
    }

    /**
     * 客户端异常
     */
    static class Error implements Consumer<Throwable> {
        private String id;
        private String remoteAddr;

        public Error(String id,String remoteAddr) {
            this.id = id;
            this.remoteAddr = remoteAddr;
        }

        @Override
        public void accept(Throwable e) {
            log.error("客户端异常,id:[{}],remote:[{}],[{}]",id,remoteAddr,e.getMessage(),e);
        }
    }
}
