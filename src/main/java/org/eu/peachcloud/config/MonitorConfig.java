package org.eu.peachcloud.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.eu.peachcloud.enums.EnableEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/14:23
 * 配置参数
 */
@Data
public class MonitorConfig {

    private String haxUrl;

    private String woUrl;

    private String vcUrl;

    private ProxyObj proxyObj;

    private Integer threadPool;

    private String accessToken;

    private String secret;

    private String openApi;

    private String messageEnable;

    private Vc vcObj;

    public MonitorConfig() {
        String propertiesURI = System.getProperty("propertiesURI");
        Assert.isTrue(CharSequenceUtil.isNotBlank(propertiesURI), "配置文件找不到");
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesURI);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.haxUrl = properties.getProperty("haxUrl");
        Assert.notBlank(haxUrl, "hax监控地址未配");
        this.woUrl = properties.getProperty("woUrl");
        Assert.notBlank(woUrl, "woiden监控地址未配");
        this.vcUrl = properties.getProperty("vcUrl");
        Assert.notBlank(vcUrl, "vc监控地址未配");

        String enable = properties.getProperty("proxy.enable");
        String address = properties.getProperty("proxy.address");
        String portStr = properties.getProperty("proxy.port");
        if (ObjectUtil.equals(enable, EnableEnum.ENABLE.getCode())) {
            ProxyObj obj = new ProxyObj();
            obj.setEnable(Integer.parseInt(enable));
            obj.setAddress(address);
            obj.setPort(Integer.parseInt(portStr));
            this.proxyObj = obj;
        }
        String pool = properties.getProperty("thread.pool");
        this.threadPool = CharSequenceUtil.isEmpty(pool) ? 1 : Integer.parseInt(pool);
        this.accessToken = properties.getProperty("message.accessToken");
        this.openApi = properties.getProperty("message.openApi");
        this.secret = properties.getProperty("message.secret");
        String propertyEnable = properties.getProperty("message.enable");
        this.messageEnable = CharSequenceUtil.isNotBlank(propertyEnable) ? propertyEnable : "0";
        Vc vc = new Vc();
        vc.setCookie(properties.getProperty("vc.cookie"));
        vc.setName(properties.getProperty("vc.cookie.name"));
        this.vcObj = vc;
    }

    public static class ProxyObj {
        private Integer enable;
        private String address;
        private Integer port;

        public Integer getEnable() {
            return enable;
        }

        public void setEnable(Integer enable) {
            this.enable = enable;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public static class Vc{
        private String cookie;

        private String name;

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
