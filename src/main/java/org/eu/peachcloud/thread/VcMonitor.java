package org.eu.peachcloud.thread;

import lombok.extern.slf4j.Slf4j;
import org.eu.peachcloud.commen.BasicMonitor;
import org.eu.peachcloud.enums.ChannelEnum;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/14/14:01
 * vc 机器监听器
 */
@Slf4j
public class VcMonitor extends BasicMonitor implements Runnable {

    private final String url;

    private static final ChannelEnum channelEnum = ChannelEnum.VC;

    public VcMonitor(String url) {
        this.url = url;
    }

    public void run() {
        listen(url);
    }

    public void listen(String url) {
        log.info("开始监听：{}", channelEnum.getName());
        sendOrAcceptResult(url, channelEnum);
    }
}
