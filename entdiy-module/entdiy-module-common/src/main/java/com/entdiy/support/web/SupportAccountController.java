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
package com.entdiy.support.web;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.AccountService;
import com.entdiy.core.service.Validation;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.captcha.CaptchaUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.annotation.AuthAccount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SupportAccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/pub/account/password/forget", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult forgetPasswordSave(HttpServletRequest request, @RequestParam("uid") String uid,
                                              @RequestParam(value = "authType", defaultValue = "site") String authType) {
        //二次校验验证码避免绕过表单校验的恶意请求
        CaptchaUtils.assetValidateCaptchaCode(request, "captcha");

        Account account = accountService.findByUsername(Account.AuthTypeEnum.valueOf(authType), uid);
        if (account == null) {
            return OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理");
        }
        String email = account.getEmail();
        if (StringUtils.isBlank(email)) {
            return OperationResult.buildFailureResult("当前账号未设定注册邮箱，请联系管理员先设置邮箱后再进行此操作");
        }

        accountService.requestResetPassword(account);
        return OperationResult.buildSuccessResult("找回密码请求处理成功。重置密码邮件已发送至：" + email);
    }

    @RequestMapping(value = "/pub/account/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult resetPasswordSave(@RequestParam("uid") String uid,
                                             @RequestParam(value = "authType", defaultValue = "site") String authType,
                                             @RequestParam("email") String email,
                                             @RequestParam("code") String code,
                                             @RequestParam("newpasswd") String newpasswd) {
        Validation.notDemoMode();
        Account account = accountService.findByUsername(Account.AuthTypeEnum.valueOf(authType), uid);
        if (account == null) {
            return OperationResult.buildFailureResult("未找到匹配账号信息，请联系管理员处理");
        }
        if (!email.equalsIgnoreCase(account.getEmail())) {
            return OperationResult.buildFailureResult("登录账号和邮箱地址不匹配，请检查或联系管理员");
        }
        if (code.equals(account.getRandomCode())) {
            //user.setRandomCode(null);
            //更新密码失效日期为6个月后
            account.setCredentialsExpireDate(DateUtils.currentDateTime().plusMonths(6).toLocalDate());
            accountService.save(account, newpasswd);
            return OperationResult.buildSuccessResult("密码重置成功，您可以马上使用新设定密码登录系统啦").setRedirect("/admin/login");
        } else {
            return OperationResult.buildFailureResult("验证码不正确或已失效，请尝试重新找回密码操作");
        }
    }

    @RequestMapping(value = "/account/profile/password", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult modifyPasswordSave(@AuthAccount Account account,
                                              @RequestParam("oldpasswd") String oldpasswd,
                                              @RequestParam("newpasswd") String newpasswd,
                                              @RequestParam(value = "redirectURI", required = false) String redirectURI) {
        Validation.notDemoMode();
        Validation.isTrue(!oldpasswd.equals(newpasswd), "新旧密码不可相同");
        String encodedPasswd = accountService.encodeUserPasswd(account, oldpasswd);
        if (!encodedPasswd.equals(account.getPassword())) {
            return OperationResult.buildFailureResult("原密码不正确,请重新输入");
        } else {
            //更新密码失效日期为6个月后
            account.setCredentialsExpireDate(DateUtils.currentDateTime().plusMonths(6).toLocalDate());
            accountService.save(account, newpasswd);
            OperationResult operationResult = OperationResult.buildSuccessResult("密码修改成功,请在下次登录使用新密码");
            if (StringUtils.isNotBlank(redirectURI)) {
                operationResult.setRedirect(redirectURI);
            }
            return operationResult;
        }
    }
}
