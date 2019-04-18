/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package io.swagger.models;

import java.util.ArrayList;
import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/17 19:54
 */
public class MarkdownFiles {

    private List<MarkdownFile> markdownFiles=new ArrayList<>();

    public List<MarkdownFile> getMarkdownFiles() {
        return markdownFiles;
    }

    public void setMarkdownFiles(List<MarkdownFile> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }
}
