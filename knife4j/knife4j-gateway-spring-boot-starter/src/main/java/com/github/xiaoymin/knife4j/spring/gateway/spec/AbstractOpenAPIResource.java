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


package com.github.xiaoymin.knife4j.spring.gateway.spec;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="milo.xiaomeng@gmail.com">milo.xiaomeng@gmail.com</a>
 *     23/02/26 20:43
 * @since gateway-spring-boot-starter v4.1.0
 */
public abstract class AbstractOpenAPIResource implements Comparable<AbstractOpenAPIResource>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonIgnore
    protected final transient Integer order;
    @JsonIgnore
    protected final transient Boolean discovered;
    
    protected AbstractOpenAPIResource(Integer order, Boolean discovered) {
        this.order = order;
        this.discovered = discovered;
    }
    
    @Override
    public int compareTo(@NonNull AbstractOpenAPIResource swaggerResource) {
        int sort = this.order.compareTo(swaggerResource.getOrder());
        if (sort != 0) {
            return sort;
        }
        return this.getName().compareTo(swaggerResource.getName());
    }
    
    public abstract String getName();
    
    public Integer getOrder() {
        return order;
    }
    
    public Boolean getDiscovered() {
        return discovered;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractOpenAPIResource that = (AbstractOpenAPIResource) o;
        return Objects.equals(order, that.order) && Objects.equals(discovered, that.discovered);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(order, discovered);
    }
}
