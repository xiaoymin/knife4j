package springfox.documentation.swagger2.web;

import io.swagger.annotations.Api;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.swagger2.model.OperationSortModel;
import springfox.documentation.swagger2.model.SortModel;
import springfox.documentation.swagger2.util.Uputil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: swagger-bootstrap-ui <br/>
 * @Date: 2018/9/23 14:24 <br/>
 * @Author: baoec@drore.com
 */
@Order
@Controller
@RequestMapping("/v2")
public class SwaggerSortCtl {

    @PostMapping("/api-sorts")
    @ResponseBody
    public Map apiSorts(String tags, HttpServletRequest request) {
        List<SortModel> result = new ArrayList<SortModel>();
        List<OperationSortModel> result2 = new ArrayList<OperationSortModel>();
        WebApplicationContext wc = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        // Method 层排序
        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value=entry.getValue();
            Order order_annotation = value.getMethod().getAnnotation(Order.class);
            OperationSortModel oModel = new OperationSortModel();
            oModel.setMethod(key.getPatternsCondition().getPatterns().iterator().next().toString());
            oModel.setSort(order_annotation==null?99:Integer.valueOf(order_annotation.value()));
            result2.add(oModel);
        }
        // Ctl层排序
        Map<String, Object> beansWithAnnotation = wc.getBeansWithAnnotation(Controller.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            Order annotation = aClass.getAnnotation(Order.class);
            Api Api_annotation = aClass.getAnnotation(Api.class);
            SortModel model=new SortModel();
            model.setDescription(Uputil.UpperCase(aClass.getSimpleName()));
            model.setSort(annotation==null?999:Integer.valueOf(annotation.value()));
            model.setName(Api_annotation==null?Uputil.UpperCase(aClass.getSimpleName()):Api_annotation.tags()[0].toString());
            result.add(model);
        }
        Collections.sort(result,new Comparator<SortModel> (){
            public int compare(SortModel m1, SortModel m2) {
                return m1.getSort().compareTo(m2.getSort());
            }
        });
        HashMap<Object, Object> map = new HashMap();
        map.put("tags",result);
        map.put("paths",result2);

        return map;
    }
}
