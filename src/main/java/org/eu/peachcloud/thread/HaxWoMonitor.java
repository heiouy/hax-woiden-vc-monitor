package org.eu.peachcloud.thread;

import lombok.extern.slf4j.Slf4j;
import org.eu.peachcloud.commen.BasicMonitor;
import org.eu.peachcloud.enums.ChannelEnum;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/15:59
 * hax wo 库存监听地址
 */
@Slf4j
public class HaxWoMonitor extends BasicMonitor implements Runnable {

    private final String url;

    private final ChannelEnum channelEnum;

    public HaxWoMonitor(String url, ChannelEnum channelEnum) {
        this.url = url;
        this.channelEnum = channelEnum;
    }

    public void run() {
        listen(url);
    }

    public void listen(String url) {
        log.info("开始监听：{}", channelEnum.getName());
        sendOrAcceptResult(url, channelEnum);
    }
}
