/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.sys.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeNestedSetEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.ClassUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "sys_Menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "菜单")
@Audited
public class Menu extends BaseNativeNestedSetEntity<Menu> {

    private static final long serialVersionUID = 2860233299443173932L;

    private static final Logger logger = LoggerFactory.getLogger(Menu.class);

    @MetaData(value = "名称")
    @Column(nullable = false, length = 32)
    private String name;

    @MetaData(value = "菜单路径")
    @Column(nullable = false, length = 255, unique = true)
    private String path;

    @MetaData(value = "描述")
    @Column(length = 1000)
    @JsonIgnore
    private String description;

    @MetaData(value = "菜单URL")
    @Column(length = 256)
    private String url;

    @MetaData(value = "图标样式")
    @Column(length = 128)
    private String style;

    @MetaData(value = "展开标识", tooltips = "是否默认展开菜单组")
    private Boolean initOpen = Boolean.FALSE;

    @MetaData(value = "对应Web Controller类名")
    @Column(length = 256)
    private String controllerClass;

    @MetaData(value = "对应Web Controller调用方法名")
    @Column(length = 128)
    private String controllerMethod;

    @Override
    @Transient
    public String getDisplay() {
        return path;
    }

    @MetaData(value = "缓存Web Controller调用方法", comments = "用于缓存记录对应的Controller方法，方便权限判断比较")
    @Transient
    private Method mappingMethod;

    @Transient
    @JsonIgnore
    public Method getMappingMethod() {
        if (mappingMethod != null) {
            return mappingMethod;
        }
        //基于记录的Controller类和方法信息构造MethodInvocation,用于后续调用shiro的拦截器进行访问权限比对
        if (StringUtils.isNotBlank(getControllerMethod())) {
            try {
                final Class<?> clazz = ClassUtils.forName(getControllerClass());
                Method[] methods = clazz.getMethods();
                for (final Method method : methods) {
                    if (method.getName().equals(getControllerMethod())) {
                        RequestMapping rm = method.getAnnotation(RequestMapping.class);
                        if (rm.method() == null || rm.method().length == 0 || ArrayUtils.contains(rm.method(), RequestMethod.GET)) {
                            mappingMethod = method;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Menu data process error", e);
            }
        }
        return mappingMethod;
    }

    public Map<String, Object> buildMapDataForTreeDisplay() {
        //组装zTree结构数据
        Map<String, Object> item = Maps.newHashMap();
        item.put("id", this.getId());
        item.put("parent", this.getParent().getId());
        item.put("name", this.getName());
        item.put("display", this.getName());
        item.put("open", true);
        item.put("hasChildren", this.hasChildren());
        return item;
    }
}
