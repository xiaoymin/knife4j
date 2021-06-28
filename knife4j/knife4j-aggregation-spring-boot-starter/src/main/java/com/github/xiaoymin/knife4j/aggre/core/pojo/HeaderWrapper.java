package com.github.xiaoymin.knife4j.aggre.core.pojo;

/***
 * 自定义header包装bean
 * @since:knife4j-aggregation-spring-boot-starter 2.0.8
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2021/03/08 20:30
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
