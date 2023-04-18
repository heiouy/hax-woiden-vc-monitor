package org.eu.peachcloud.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/15:19
 * 代理相关枚举
 */
public enum EnableEnum {
    ENABLE("1","启用");

    private final String code;
    private final String desc;

    EnableEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
