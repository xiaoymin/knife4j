/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.xiaoymin.knife4j.aggre.repository;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.aggre.core.ext.PoolingConnectionManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zc
 * @date 2023/3/20 19:11
 */
public class PolarisRepository {
    
    public static void main(String[] args) throws IOException {
        final PoolingConnectionManager poolingConnectionManager = new PoolingConnectionManager();
        
        String api = "http://192.168.1.200:8080/core/v1/user/login";
        HttpPost post = new HttpPost(api);
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("name", "polaris"));
        // 访问Nacos时bug
        // https://gitee.com/xiaoym/knife4j/issues/I4UF84
        pairs.add(new BasicNameValuePair("password", "polaris"));
        post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        CloseableHttpResponse response = poolingConnectionManager.getClient().execute(post);
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (StrUtil.isNotBlank(content)) {
                    JsonElement jsonElement = JsonParser.parseString(content);
                    if (jsonElement != null && jsonElement.isJsonObject()) {
                        System.out.println(jsonElement);
                    }
                }
            } else {
                post.abort();
            }
        }
    }
}
