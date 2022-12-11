/*
 * Copyright (C) 2022 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */
package com.github.xiaoymin.knife4j.proxy.pojo;

import lombok.Data;
import org.springframework.http.MediaType;

/**
 * webjar内容
 * @since:knife4j-desktop
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/12/11 18:40
 */
@Data
public class WebJarFile {

    private String webjar;
    private MediaType mediaType;
    private String content;
}
