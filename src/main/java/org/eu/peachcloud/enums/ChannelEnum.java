package org.eu.peachcloud.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/21:12
 * 机器渠道枚举
 */
public enum ChannelEnum {
    HAX("hax"),
    WOIDEN("woiden"),
    VC("vc");

    private final String name;

    ChannelEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
