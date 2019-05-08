<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>忘记密码</title>
</head>
<body>
<form id="password-forget-form" class="form form-horizontal form-bordered form-bordered-blank"
      method="post" data-validation='true'
      action="/pub/account/password/forget">
    <input type="hidden" name="authType" value="admin">
    <h3>找回密码</h3>
    <div class="form-body">
        <div class="form-group">
            <div class="controls controls-clearfix">
                <p class="form-control-static">输入您注册时填写的登录账号或邮箱地址.</p>
                <p class="form-control-static">如果未设置注册邮箱或遗忘相关注册信息请联系管理员协助处理.</p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <div class="controls controls-clearfix">
                        <input class="form-control" type="text"
                               autocomplete="off" data-input-icon="fa-user"
                               placeholder="填写登录账号或注册邮箱" name="uid" required="true"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="controls controls-clearfix">
                <input class="form-control" data-toggle="captcha-code"
                       type="text" name="captcha" required="true" data-input-icon="fa-qrcode"/>
            </div>
        </div>
        <c:if test="${!mailServiceEnabled}">
            <div class="note note-warning" style="margin-bottom: 0px">
                <p>系统当前未开启邮件服务，暂时无法提供找回密码服务！</p>
                <p>若有疑问请联系告知管理员！</p>
            </div>
        </c:if>
    </div>
    <div class="form-actions right">
        <a class="btn btn-info" href="javascript:history.back()">
            <i class="fa fa-undo"></i> 取消，返回登录
        </a>
        <c:if test="${mailServiceEnabled}">
            <button class="btn btn-primary" type="submit" data-post-dismiss="modal">
                <i class="fa fa-check"></i> 发送找回密码邮件
            </button>
        </c:if>
    </div>
</form>
</body>
</html>