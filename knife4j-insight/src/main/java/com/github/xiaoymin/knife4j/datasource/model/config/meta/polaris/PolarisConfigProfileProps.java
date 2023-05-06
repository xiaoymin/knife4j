package com.github.xiaoymin.knife4j.datasource.model.config.meta.polaris;

import com.github.xiaoymin.knife4j.datasource.model.config.meta.ConfigProfileProps;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zc
 * @date 2023/4/11 23:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PolarisConfigProfileProps extends ConfigProfileProps {

    private PolarisConfigProfileInfo knife4j;
}
