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
package com.entdiy.dev.demo.entity;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "demo_Product")
@MetaData(value = "商品")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class DemoProduct extends BaseNativeEntity {

    @MetaData(value = "编码")
    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @MetaData(value = "名称")
    @Column(length = 256, nullable = false)
    private String name;

    @MetaData(value = "预览图片列表，逗号分隔URL", image = true, multiple = true)
    @Column(length = 2000, nullable = false)
    @JsonView(JsonViews.Admin.class)
    private String commaPreviewImageUrls;

    @Transient
    @JsonView(JsonViews.AppDetail.class)
    public String[] getPreviewImageUrls() {
        return StringUtils.split(commaPreviewImageUrls, ",");
    }

    @Transient
    @JsonView(JsonViews.AppList.class)
    public String getMainImage() {
        return getPreviewImageUrls()[0];
    }


}
