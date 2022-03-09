package com.sanri.test.sometest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * jdk 自带的文件监听
 */
@Slf4j
public class FileWatch {

	/**
	 * jdk 自带的文件监听 , 注意,不能少了最后的步骤   take.reset()
	 */
	static class JdkFileWatch {
		WatchService watchService;

		public JdkFileWatch(Path path) throws IOException {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.OVERFLOW);

			new Thread() {
				@Override
				public void run() {
					while (true) {
						WatchKey take = null;
						try {
							take = watchService.take();
							List<WatchEvent<?>> watchEvents = take.pollEvents();
							for (WatchEvent<?> watchEvent : watchEvents) {
								WatchEvent.Kind<?> kind = watchEvent.kind();
								if (kind == StandardWatchEventKinds.OVERFLOW) {
									log.info("漏掉的事件???");
									continue;
								}

								Path targetPath = (Path) watchEvent.context();
								System.out.println(targetPath + " 发生事件 " + kind);

								if(!take.reset()){
									break;
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			}.start();
		}

	}

	/**
	 * 这个 apache 的文件监听可以区分是文件夹创建还是文件创建
	 * 场景更详细一点
	 */
	static class ApacheFileObserver{

		public ApacheFileObserver(File dir) throws Exception {
			FileAlterationObserver observer = new FileAlterationObserver(dir);

			observer.addListener(new ListenerAdaptor());

			// 创建文件变化监听器
			FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(TimeUnit.SECONDS.toMillis(2), observer);
			// 开启监听
			fileAlterationMonitor.start();
		}

		static class ListenerAdaptor extends FileAlterationListenerAdaptor {
			@Override
			public void onFileCreate(File file) {
				System.out.println("创建文件"+file);
			}

			@Override
			public void onFileDelete(File file) {
				System.out.println("删除文件"+file);
			}

			@Override
			public void onDirectoryChange(File directory) {
				System.out.println("目录改变?"+directory);
			}
		}

	}

	public static void main(String[] args) throws Exception {
//		JdkFileWatch jdkFileWatch = new JdkFileWatch(Paths.get("d:/test"));
		ApacheFileObserver apacheFileObserver = new ApacheFileObserver(new File("d:/test"));
	}
}
