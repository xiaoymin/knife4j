package com.github.xiaoymin.knife4j.spring.plugin.em.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.github.xiaoymin.knife4j.spring.plugin.em.constant.EnumConstant;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * swagger枚举入参定义说明
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@SuppressWarnings(value = "all")
public class EnumParameterBuilderPlugin implements ParameterBuilderPlugin, OperationBuilderPlugin {

    private static final Joiner joiner = Joiner.on(";");

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(ParameterContext context) {
        Class<?> type = context.resolvedMethodParameter().getParameterType().getErasedType();
        if (Enum.class.isAssignableFrom(type)) {
            Object[] enumConstants = type.getEnumConstants();
            List<String> displayValues = Arrays.stream(enumConstants).filter(Objects::nonNull)
                    .filter(o -> !Objects.isNull(ReflectionUtils.findField(o.getClass(), EnumConstant.VALUE))).map(item -> {
                        Class<?> currentClass = item.getClass();

                        Field indexField = ReflectionUtils.findField(currentClass, EnumConstant.VALUE);
                        ReflectionUtils.makeAccessible(indexField);
                        Object value = ReflectionUtils.getField(indexField, item);

                        Field descField = ReflectionUtils.findField(currentClass, EnumConstant.DESC);
                        ReflectionUtils.makeAccessible(descField);
                        Object desc = ReflectionUtils.getField(descField, item);
                        return value.toString();

                    }).collect(Collectors.toList());

            ParameterBuilder parameterBuilder = context.parameterBuilder();
            AllowableListValues values = new AllowableListValues(displayValues, "LIST");
            parameterBuilder.allowableValues(values);
//            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> parameters = context.getParameters();
        parameters.forEach(parameter -> {
            ResolvedType parameterType = parameter.getParameterType();
            Class<?> clazz = parameterType.getErasedType();
            if (Enum.class.isAssignableFrom(clazz)) {
                Object[] enumConstants = clazz.getEnumConstants();

                List<String> displayValues = Arrays.stream(enumConstants).filter(Objects::nonNull).
                        filter(o -> !Objects.isNull(ReflectionUtils.findField(o.getClass(), EnumConstant.VALUE))).map(item -> {
                    Class<?> currentClass = item.getClass();

                    Field indexField = ReflectionUtils.findField(currentClass, EnumConstant.VALUE);
                    ReflectionUtils.makeAccessible(indexField);
                    Object value = ReflectionUtils.getField(indexField, item);

                    Field descField = ReflectionUtils.findField(currentClass, EnumConstant.DESC);
                    ReflectionUtils.makeAccessible(descField);
                    Object desc = ReflectionUtils.getField(descField, item);
                    return value + ":" + desc;

                }).collect(Collectors.toList());

                OperationBuilder operationBuilder = context.operationBuilder();
                Field parametersField = ReflectionUtils.findField(operationBuilder.getClass(), "parameters");
                ReflectionUtils.makeAccessible(parametersField);
                List<Parameter> list = (List<Parameter>) ReflectionUtils.getField(parametersField, operationBuilder);

                String parameterName = parameter.defaultName().orElse("");
                Predicate condition = (str) -> StringUtils.equals(str.toString(), parameterName);
                Parameter currentParameter = list.stream().filter((p) -> (condition.test(p.getName()))).findFirst().orElse(null);

                if (currentParameter != null) {
                    Field description = ReflectionUtils.findField(currentParameter.getClass(), "description");
                    ReflectionUtils.makeAccessible(description);
                    Object field = ReflectionUtils.getField(description, currentParameter);
                    ReflectionUtils.setField(description, currentParameter, field + "(" + joiner.join(displayValues) + ")");
                }
            }
        });
    }
}
