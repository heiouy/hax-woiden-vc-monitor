package org.eu.peachcloud.commen;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.eu.peachcloud.config.MonitorConfig;
import org.eu.peachcloud.enums.ChannelEnum;
import org.eu.peachcloud.enums.EnableEnum;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/15:38
 * 基础监听器
 */
@Slf4j
public abstract class BasicMonitor {

    private final MonitorConfig config = ConfigFactory.getConfigInstance();

    private int errorIndex = 0;

    /**
     * 监听入口
     *
     * @param url 被监听地址
     */
    public abstract void listen(String url);

    public void sendOrAcceptResult(String url, ChannelEnum channelEnum) {
        List<String> machineArray = new ArrayList<String>();
        while (true) {
            List<String> machineList = null;
            try {
                machineList = ObjectUtil.equals(channelEnum, ChannelEnum.VC) ?
                        sendRequestByCookie(url) : sendRequest(url);
            } catch (Exception e) {
                errorIndex++;
                log.error("监听渠道{}发生异常，异常原因={}", channelEnum.getName(), e.getMessage());
                log.error("监听渠道{}发生异常{}次，暂时休眠线程一分钟", channelEnum.getName(), errorIndex);
                try {
                    sleep(1000L * 60);
                } catch (InterruptedException ex) {
                    log.error("监听渠道{}发生异常，暂时休眠线程异常", channelEnum.getName());
                }
            }

            int errorMaxNum = 20;
            if (errorIndex == errorMaxNum) {
                log.error("监听渠道{}发生异常，已停止线程", channelEnum.getName());
                frontMessageSend("监听异常，已停止线程。", channelEnum);
                break;
            }

            machineList = ObjectUtil.isNotNull(machineList) ? machineList : new ArrayList<String>();
            if (!isListEquals(machineArray, machineList)) {
                if (machineList.size() <= 1 && ObjectUtil.equals(machineList.get(0), "-select-")) {
                    List<String> emptyArray = new ArrayList<String>();
                    emptyArray.add("没有剩余库存");
                    frontMessageSend(emptyArray, channelEnum);
                } else {
                    log.info("监听到库存变化时间：{}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    machineList.remove(0);
                    frontMessageSend(machineList, channelEnum);
                }
                machineArray.clear();
                machineArray.addAll(machineList);
                errorIndex = 0;
            }
        }
    }

    /**
     * 比较两个list是否一致
     *
     * @param list        list1
     * @param compareList 比较集合
     * @return 是否一致
     */
    private boolean isListEquals(List<String> list, List<String> compareList) {
        int size = list.size();
        int compareSize = compareList.size();
        if (size != compareSize) {
            return false;
        }
        String s = list.toString();
        String s1 = compareList.toString();
        return s.hashCode() == s1.hashCode();
    }

    /**
     * 底层请求告警逻辑
     *
     * @param url 被监听地址
     */
    private List<String> sendRequest(String url) {
        HttpRequest request = HttpUtil.createRequest(Method.GET, url);
        MonitorConfig.ProxyObj proxyObj = config.getProxyObj();
        if (ObjectUtil.isNotNull(proxyObj) && ObjectUtil.equals(proxyObj.getEnable(), EnableEnum.ENABLE.getCode())) {
            request.setHttpProxy(proxyObj.getAddress(), proxyObj.getPort());
        }
        HttpResponse execute = request.execute();
        String body = execute.body();
        Document doc = Jsoup.parse(body);
        return documentBodyToList(doc);
    }

    private List<String> sendRequestByCookie(String url) {
        Connection connect = Jsoup.connect(url);
        connect.cookie(config.getVcObj().getName(), config.getVcObj().getCookie());
        MonitorConfig.ProxyObj proxyObj = config.getProxyObj();
        if (ObjectUtil.isNotNull(proxyObj) && ObjectUtil.equals(proxyObj.getEnable(), EnableEnum.ENABLE.getCode())) {
            connect.proxy(proxyObj.getAddress(), proxyObj.getPort());
        }
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentBodyToList(document);
    }

    private List<String> documentBodyToList(Document document) {
        if (ObjectUtil.isNull(document)) {
            return new ArrayList<String>();
        }
        Element datacenter = document.getElementById("datacenter");
        List<String> machineList = null;
        if (datacenter != null) {
            machineList = new ArrayList<String>();
            Elements options = datacenter.getElementsByTag("option");
            for (Element option : options) {
                machineList.add(option.text());
            }
        }
        Assert.notNull(machineList, "抓取异常");
        return machineList;
    }

    /**
     * 消息发送前操作
     *
     * @param list        列表
     * @param channelEnum 渠道枚举
     */
    public void frontMessageSend(List<String> list, ChannelEnum channelEnum) {
        if (ObjectUtil.equals(config.getMessageEnable(), EnableEnum.ENABLE.getCode())) {
            DingTalkMessageSender messageSender = new DingTalkMessageSender();
            messageSender.setTitle(channelEnum.getName() + "库存监控");
            messageSender.buildText(list, channelEnum);
            messageSender.send();
        }
    }

    public void frontMessageSend(String message, ChannelEnum channelEnum) {
        if (ObjectUtil.equals(config.getMessageEnable(), EnableEnum.ENABLE.getCode())) {
            DingTalkMessageSender messageSender = new DingTalkMessageSender();
            messageSender.setTitle(channelEnum.getName() + "库存监控");
            messageSender.setText(message);
            messageSender.send();
        }
    }
}
