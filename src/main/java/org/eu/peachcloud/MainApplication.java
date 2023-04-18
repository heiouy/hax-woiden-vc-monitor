package org.eu.peachcloud;

import lombok.extern.slf4j.Slf4j;
import org.eu.peachcloud.commen.ConfigFactory;
import org.eu.peachcloud.config.MonitorConfig;
import org.eu.peachcloud.enums.ChannelEnum;
import org.eu.peachcloud.thread.HaxWoMonitor;
import org.eu.peachcloud.thread.VcMonitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/11:16
 * 入口
 */
@Slf4j
public class MainApplication {

    public static void main(String[] args) {
        log.info("监控启动...");
        MonitorConfig config = ConfigFactory.getConfigInstance();
        ExecutorService executorService = Executors.newFixedThreadPool(config.getThreadPool());
        HaxWoMonitor haxMonitor = new HaxWoMonitor(config.getHaxUrl(), ChannelEnum.HAX);
        HaxWoMonitor woMonitor = new HaxWoMonitor(config.getWoUrl(), ChannelEnum.WOIDEN);
        VcMonitor vcMonitor = new VcMonitor(config.getVcUrl());
        executorService.submit(haxMonitor);
        executorService.submit(woMonitor);
        executorService.submit(vcMonitor);
    }
}
