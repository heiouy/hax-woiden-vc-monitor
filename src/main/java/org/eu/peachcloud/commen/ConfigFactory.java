package org.eu.peachcloud.commen;

import org.eu.peachcloud.config.MonitorConfig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/15:49
 * 配置静态工厂
 */
public class ConfigFactory {

    private static class InstanceMonitorConfig{
        public static final MonitorConfig monitorConfig = new MonitorConfig();
    }

    private ConfigFactory() {}

    public static MonitorConfig getConfigInstance(){
        return InstanceMonitorConfig.monitorConfig;
    }
}
