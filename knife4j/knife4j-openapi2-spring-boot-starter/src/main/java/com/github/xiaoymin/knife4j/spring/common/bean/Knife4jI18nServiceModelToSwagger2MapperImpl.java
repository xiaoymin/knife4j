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


package com.github.xiaoymin.knife4j.spring.common.bean;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import org.springframework.context.MessageSource;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.swagger2.mappers.*;

import java.util.*;

/**
 * Support i18n with message.properties,
 * Rewrite the ServiceModelToSwagger2Mapper implementation class, and some information is obtained from the i18n configuration information
 * @since  4.0.0
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/21 09:54
 */
public class Knife4jI18nServiceModelToSwagger2MapperImpl extends ServiceModelToSwagger2Mapper {
    
    final MessageSource messageSource;
    final Locale locale;
    final ModelMapper modelMapper;
    final ParameterMapper parameterMapper;
    final SecurityMapper securityMapper;
    final LicenseMapper licenseMapper;
    final VendorExtensionsMapper vendorExtensionsMapper;
    
    public Knife4jI18nServiceModelToSwagger2MapperImpl(MessageSource messageSource, Locale locale, ModelMapper modelMapper, ParameterMapper parameterMapper, SecurityMapper securityMapper,
                                                       LicenseMapper licenseMapper, VendorExtensionsMapper vendorExtensionsMapper) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.modelMapper = modelMapper;
        this.parameterMapper = parameterMapper;
        this.securityMapper = securityMapper;
        this.licenseMapper = licenseMapper;
        this.vendorExtensionsMapper = vendorExtensionsMapper;
    }
    
    public Swagger mapDocumentation(Documentation from) {
        if (from == null) {
            return null;
        } else {
            Swagger swagger = new Swagger();
            swagger.setVendorExtensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            swagger.setSchemes(this.mapSchemes(from.getSchemes()));
            swagger.setPaths(this.mapApiListings(from.getApiListings()));
            swagger.setHost(from.getHost());
            swagger.setDefinitions(this.modelsFromApiListings(from.getApiListings()));
            swagger.setSecurityDefinitions(this.securityMapper.toSecuritySchemeDefinitions(from.getResourceListing()));
            swagger.setInfo(this.mapApiInfo(this.fromResourceListingInfo(from)));
            swagger.setBasePath(from.getBasePath());
            swagger.setTags(this.tagSetToTagList(from.getTags()));
            List<String> list2 = from.getConsumes();
            if (list2 != null) {
                swagger.setConsumes(new ArrayList(list2));
            }
            
            List<String> list3 = from.getProduces();
            if (list3 != null) {
                swagger.setProduces(new ArrayList(list3));
            }
            
            return swagger;
        }
    }
    
    protected Info mapApiInfo(ApiInfo from) {
        if (from == null) {
            return null;
        } else {
            Info info = new Info();
            info.setLicense(this.licenseMapper.apiInfoToLicense(from));
            info.setVendorExtensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            info.setTermsOfService(from.getTermsOfServiceUrl());
            info.setContact(this.map(from.getContact()));
            info.setVersion(from.getVersion());
            info.setTitle(from.getTitle());
            info.setDescription(from.getDescription());
            return info;
        }
    }
    
    protected Contact map(springfox.documentation.service.Contact from) {
        if (from == null) {
            return null;
        } else {
            Contact contact = new Contact();
            contact.setName(from.getName());
            contact.setUrl(from.getUrl());
            contact.setEmail(from.getEmail());
            return contact;
        }
    }
    
    protected Operation mapOperation(springfox.documentation.service.Operation from) {
        if (from == null) {
            return null;
        } else {
            Operation operation = new Operation();
            operation.setSecurity(this.mapAuthorizations(from.getSecurityReferences()));
            operation.vendorExtensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            operation.setDescription(messageSource.getMessage(from.getNotes(), null, from.getNotes(), locale));
            // operation.setDescription(from.getNotes());
            operation.setOperationId(from.getUniqueId());
            operation.setResponses(this.mapResponseMessages(from.getResponseMessages()));
            operation.setSchemes(this.stringSetToSchemeList(from.getProtocol()));
            // operation.setSummary(from.getSummary());
            operation.setSummary(messageSource.getMessage(from.getSummary(), null, from.getSummary(), locale));
            Set<String> set = from.getConsumes();
            if (set != null) {
                operation.setConsumes(new ArrayList(set));
            }
            
            Set<String> set1 = from.getProduces();
            if (set1 != null) {
                operation.setProduces(new ArrayList(set1));
            }
            Set<String> tags = from.getTags();
            if (CollectionUtils.isNotEmpty(tags)) {
                List<String> i18nTags = new ArrayList<>(tags.size());
                Iterator<String> it = from.getTags().iterator();
                while (it.hasNext()) {
                    String tagName = it.next();
                    i18nTags.add(StrUtil.isNotBlank(tagName) ? messageSource.getMessage(tagName, null, tagName, locale) : " ");
                }
                operation.setTags(i18nTags);
            } else {
                operation.setTags(null);
            }
            
            if (from.getDeprecated() != null) {
                operation.setDeprecated(Boolean.parseBoolean(from.getDeprecated()));
            }
            
            operation.setParameters(this.parameterListToParameterList(from.getParameters()));
            return operation;
        }
    }
    
    protected Tag mapTag(springfox.documentation.service.Tag from) {
        if (from == null) {
            return null;
        } else {
            Tag tag = new Tag();
            tag.setVendorExtensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            tag.setName(messageSource.getMessage(from.getName(), null, from.getName(), locale));
            tag.setDescription(messageSource.getMessage(from.getDescription(), null, from.getDescription(), locale));
            return tag;
        }
    }
    
    private ApiInfo fromResourceListingInfo(Documentation documentation) {
        if (documentation == null) {
            return null;
        } else {
            ResourceListing resourceListing = documentation.getResourceListing();
            if (resourceListing == null) {
                return null;
            } else {
                ApiInfo info = resourceListing.getInfo();
                return info == null ? null : info;
            }
        }
    }
    
    protected List<Tag> tagSetToTagList(Set<springfox.documentation.service.Tag> set) {
        if (set == null) {
            return null;
        } else {
            List<Tag> list = new ArrayList(set.size());
            Iterator var3 = set.iterator();
            
            while (var3.hasNext()) {
                springfox.documentation.service.Tag tag = (springfox.documentation.service.Tag) var3.next();
                list.add(this.mapTag(tag));
            }
            
            return list;
        }
    }
    
    protected List<Scheme> stringSetToSchemeList(Set<String> set) {
        if (set == null) {
            return null;
        } else {
            List<Scheme> list = new ArrayList(set.size());
            Iterator var3 = set.iterator();
            
            while (var3.hasNext()) {
                String string = (String) var3.next();
                list.add(Enum.valueOf(Scheme.class, string));
            }
            
            return list;
        }
    }
    
    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        if (list == null) {
            return null;
        } else {
            List<Parameter> list1 = new ArrayList(list.size());
            for (springfox.documentation.service.Parameter param : list) {
                String description = messageSource.getMessage(param.getDescription(), null, param.getDescription(), locale);
                springfox.documentation.service.Parameter parameter = new springfox.documentation.service.Parameter(param.getName(), description, param.getDefaultValue(), param.isRequired(),
                        param.isAllowMultiple(), param.isAllowEmptyValue(), param.getModelRef(), param.getType(), param.getAllowableValues(), param.getParamType(), param.getParamAccess(),
                        param.isHidden(), param.getPattern(), param.getCollectionFormat(), param.getOrder(), param.getScalarExample(), param.getExamples(), param.getVendorExtentions());
                list1.add(parameterMapper.mapParameter(parameter));
            }
            return list1;
        }
    }
    
    Map<String, Model> modelsFromApiListings(Map<String, List<ApiListing>> apiListings) {
        Map<String, springfox.documentation.schema.Model> definitions = new TreeMap<>();
        apiListings.values().stream()
                .flatMap(Collection::stream)
                .forEachOrdered(each -> definitions.putAll(each.getModels()));
        return this.modelMapper.mapModels(definitions);
    }
}
