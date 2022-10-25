package com.github.xiaoymin.knife4j.spring.plugin.em.plugin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * <p>
 * swagger LongToString定义说明
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@SuppressWarnings(value = "all")
public class LongToStringModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    /** {@inheritDoc} */
    @Override
    public void apply(ModelPropertyContext context) {
        Optional<BeanPropertyDefinition> optional = context.getBeanPropertyDefinition();
        if (!optional.isPresent()) {
            return;
        }

        final Class<?> fieldType = optional.get().getField().getRawType();

        addDescForLongId(context, fieldType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }


    private void addDescForLongId(ModelPropertyContext context, Class<?> fieldType) {
        String fieldName = context.getBeanPropertyDefinition().get().getName();
        Class<?> declaringClass = context.getBeanPropertyDefinition().get().getField().getDeclaringClass();
        Field field = ReflectionUtils.findField(declaringClass, fieldName);
        JsonSerialize annotation = AnnotationUtils.findAnnotation(field, JsonSerialize.class);
        if (annotation != null) {
            if (ToStringSerializer.class.equals(annotation.using())) {
                context.getBuilder().type(context.getResolver().resolve(String.class));
            }
        }
    }
}
