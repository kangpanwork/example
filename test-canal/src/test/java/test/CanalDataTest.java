package test;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class CanalDataTest {

    @Test
    public void test(){
//        String zookeeperHost = ConfigFace.getServerConfig().getString("zookeeper/host");
        String zookeeperHost = "10.101.40.134:2181";
        String canalInstanceName = "example";
        CanalConnector connector = CanalConnectors.newClusterConnector(zookeeperHost,canalInstanceName, "", "");
        try {
            connector.connect();
            connector.subscribe("user_db.*");//Canal client端进行过滤
            //    connector.subscribe();//Canal Server端进行过滤
            while (true) {
                Message message = connector.getWithoutAck(1000); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
//                Thread.sleep(1000);
                } else {
//                action(message.getEntries());//进行自己的业务处理
                    List<CanalEntry.Entry> entries = message.getEntries();
                    System.out.println(entries);
                }
                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
        } catch (Exception e) {
            connector.rollback();
            log.error("从canal 中获取数据出错！", e);
        } finally {
            connector.disconnect();
        }
    }
}
