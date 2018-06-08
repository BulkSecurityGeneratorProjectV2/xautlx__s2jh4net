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
package com.entdiy.auth.service;

import com.entdiy.auth.dao.PrivilegeDao;
import com.entdiy.auth.dao.RoleDao;
import com.entdiy.auth.dao.UserDao;
import com.entdiy.auth.dao.UserExtDao;
import com.entdiy.auth.entity.*;
import com.entdiy.core.security.AuthContextHolder;
import com.entdiy.core.security.AuthUserDetails;
import com.entdiy.core.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService extends BaseService<User, Long> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserExtDao userExtDao;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RoleDao roleDao;

    @Transactional(readOnly = true)
    public User findByAccount(Account account) {
        return findOne(account.getId());
    }

    @Transactional(readOnly = true)
    public User findCurrentAuthUser() {
        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails == null) {
            return null;
        }
        return findOne(authUserDetails.getAccountId());
    }

    @Override
    public User save(User entity) {
        if (entity.isNew()) {
            entity.setId(entity.getAccount().getId());
        }
        return super.save(entity);
    }

    public UserExt saveExt(UserExt entity) {
        return userExtDao.save(entity);
    }

    public User saveCascadeAccount(User entity, String rawPassword) {
        if (StringUtils.isNotBlank(rawPassword)) {
            accountService.save(entity.getAccount(), rawPassword);
        }
        return save(entity);
    }

    @Transactional(readOnly = true)
    public List<Role> findRoles(User user) {
        return roleDao.findRolesForUser(user);
    }

    @Transactional(readOnly = true)
    public List<Privilege> findPrivileges(Set<String> roleCodes) {
        return privilegeDao.findPrivileges(roleCodes);
    }
}
