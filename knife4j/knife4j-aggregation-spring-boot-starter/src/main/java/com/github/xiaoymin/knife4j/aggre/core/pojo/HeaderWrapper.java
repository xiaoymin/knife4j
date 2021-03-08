package com.github.xiaoymin.knife4j.aggre.core.pojo;

/**
 * @author: chenzf
 * @create: 2021/3/8 8:30 下午
 * @description: 自定义header包装bean
 */
public class HeaderWrapper {

    /**
     * header名
     */
    private String name;

    /**
     * header值
     */
    private String value;

    public HeaderWrapper() {
    }

    public HeaderWrapper(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
