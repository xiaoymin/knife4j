package springfox.documentation.swagger2.model;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-ui-plugin <br/>
 * @Date: 2018/9/23 21:05 <br/>
 * @Author: baoec@drore.com
 */
public class SortModel {

    private String name;
    private String description;
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
