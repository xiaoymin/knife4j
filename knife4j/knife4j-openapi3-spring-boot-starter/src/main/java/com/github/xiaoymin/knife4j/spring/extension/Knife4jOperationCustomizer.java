package com.github.xiaoymin.knife4j.spring.extension;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.core.conf.ExtensionsConstants;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.v3.oas.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2023/2/26 12:14
 * @since:knife4j
 */
@Slf4j
public class Knife4jOperationCustomizer implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        // 解析支持作者、接口排序
        // https://gitee.com/xiaoym/knife4j/issues/I6FB9I
        ApiOperationSupport operationSupport = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiOperationSupport.class);
        if (operationSupport != null){
            if (StrUtil.isNotBlank(operationSupport.author())){
                operation.addExtension(ExtensionsConstants.EXTENSION_AUTHOR,operationSupport.author());
            }
            if (operationSupport.order()!=0){
                operation.addExtension(ExtensionsConstants.EXTENSION_ORDER,operationSupport.order());
            }
        }
        return operation;
    }
}
