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
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.sys.service.DataDictService;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "sys_DataDict", uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "primaryKey", "secondaryKey"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "数据字典")
@Audited
public class DataDict extends BaseNativeNestedSetEntity<DataDict> implements Comparable<DataDict> {

    private static final long serialVersionUID = 5732022663570063926L;

    /**
     * 字典数据的主标识，绝大部分情况对于单一主标识就能确定唯一性的字典数据只需维护此字段值即可
     * 注意：primaryKey+secondaryKey+parent唯一性约束
     */
    @MetaData(value = "主标识")
    @Column(length = 128, nullable = false)
    @JsonView(JsonViews.App.class)
    private String primaryKey;

    /**
     * 字典数据的secondaryKey值，如果primaryKey值不能单一确定唯一性则可以启用secondaryKey值进行组合唯一控制
     */
    @MetaData(value = "次标识")
    @Column(length = 128)
    @JsonView(JsonViews.App.class)
    private String secondaryKey;

    /**
     * 字典数据对应的数据Value值
     * 大部分情况一般都是key-value形式的数据，只需要维护primaryKey和primaryValue即可，
     * 然后通过{@link DataDictService#findMapDataByRootPrimaryKey(String)}即可快速返回key-value形式的Map数据
     */
    @MetaData(value = "主要数据")
    @JsonView(JsonViews.App.class)
    private String primaryValue;

    /**
     * 字典数据对应的补充数据Value值，如果除了primaryValue业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findChildrenByRootPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "次要数据")
    @JsonView(JsonViews.App.class)
    private String secondaryValue;

    /**
     * 字典数据对应的补充数据大文本类型Value值，如果除了primaryValue业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link com.entdiy.sys.service.DataDictService#findChildrenByRootPrimaryKey(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(value = "大文本数据", tooltips = "以CLOB大文本方式存储用于特定的大文本数据配置")
    @Lob
    @JsonView(JsonViews.App.class)
    private String richTextValue;

    @Override
    @Transient
    public String getDisplay() {
        return primaryKey + ":" + primaryValue;
    }

    @Transient
    @JsonIgnore
    public String getUniqueKey() {
        StringBuilder sb = new StringBuilder();
        if (this.getParent() != null) {
            sb.append(this.getParent().getPrimaryKey() + "_");
        }
        sb.append(primaryKey);
        if (StringUtils.isNotBlank(secondaryKey)) {
            sb.append("_" + secondaryKey);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(DataDict o) {
        return this.getRgt().compareTo(o.getRgt());
    }
}
