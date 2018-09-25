package springfox.documentation.swagger2.model;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-ui-plugin <br/>
 * @Date: 2018/9/25 8:44 <br/>
 * @Author: baoec@drore.com
 */
public class OperationSortModel {
    private String method;
//    private String modify_time;
//    private String description;
    private Integer sort;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

//    public String getModify_time() {
//        return modify_time;
//    }
//
//    public void setModify_time(String modify_time) {
//        this.modify_time = modify_time;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
