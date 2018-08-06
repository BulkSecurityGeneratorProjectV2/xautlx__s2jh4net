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
package com.entdiy.sys.service;

import com.entdiy.auth.entity.Account;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.support.service.MailService;
import com.entdiy.support.service.MessagePushService;
import com.entdiy.support.service.SmsService;
import com.entdiy.sys.dao.AccountMessageDao;
import com.entdiy.sys.entity.AccountMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountMessageService extends BaseService<AccountMessage, Long> {

    private static final Logger logger = LoggerFactory.getLogger(AccountMessageService.class);

    @Autowired
    private AccountMessageDao accountMessageDao;

    @Autowired(required = false)
    private MailService mailService;

    @Autowired(required = false)
    private SmsService smsService;

    @Autowired(required = false)
    private MessagePushService messagePushService;

    /**
     * 查询用户未读消息个数
     *
     * @param account 当前登录用户
     */
    @Transactional(readOnly = true)
    public Long findCountToRead(Account account) {
        GroupPropertyFilter filter = GroupPropertyFilter.buildDefaultAndGroupFilter();
        filter.append(new PropertyFilter(MatchType.EQ, "targetAccount", account));
        filter.append(new PropertyFilter(MatchType.NU, "firstReadTime", Boolean.TRUE));
        return count(filter);
    }

    public void processUserRead(AccountMessage entity) {
        if (entity.getFirstReadTime() == null) {
            entity.setFirstReadTime(DateUtils.currentDateTime());
            entity.setLastReadTime(entity.getFirstReadTime());
            entity.setReadTotalCount(1);
        } else {
            entity.setLastReadTime(DateUtils.currentDateTime());
            entity.setReadTotalCount(entity.getReadTotalCount() + 1);
        }
        accountMessageDao.save(entity);
    }

    /**
     * 消息推送处理
     *
     * @param entity
     */
    public void pushMessage(AccountMessage entity) {
        //定向用户消息处理
        Account targetAccount = entity.getTargetAccount();

        //邮件推送处理
        if (entity.getEmailPush() && entity.getEmailPushTime() == null) {
            String email = targetAccount.getEmail();
            if (StringUtils.isNotBlank(email)) {
                mailService.sendHtmlMail(entity.getTitle(), entity.getMessage(), true, email);
                entity.setEmailPushTime(DateUtils.currentDateTime());
            }
        }

        //APP推送
        if (entity.getAppPush() && entity.getAppPushTime() == null) {
            if (messagePushService != null) {
                Boolean pushResult = messagePushService.sendPush(entity);
                if (pushResult == null || pushResult) {
                    entity.setAppPushTime(DateUtils.currentDateTime());
                }
            } else {
                logger.warn("MessagePushService implement NOT found.");
            }
        }

        accountMessageDao.save(entity);
    }

    @Override
    public AccountMessage save(AccountMessage entity) {
        super.save(entity);
        pushMessage(entity);
        return entity;
    }
}
