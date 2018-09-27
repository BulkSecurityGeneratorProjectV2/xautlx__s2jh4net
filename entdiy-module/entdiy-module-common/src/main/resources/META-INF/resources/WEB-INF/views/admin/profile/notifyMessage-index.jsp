<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form action="admin/profile/notify-message-list?readed=${param.readed}"
                      method="get" class="form-inline form-search-init" data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <div class="btn-group">
                                <a class="btn btn-default ${empty param.readed?'active':''}"
                                   href='#/admin/profile/notify-message' data-path='公告信息列表'>全部</a><a
                                    class="btn btn-default ${param.readed=='no'?'active':''}"
                                    href='#/admin/profile/notify-message?readed=no' data-path='未读公告信息列表'>未读</a><a
                                    class="btn btn-default ${param.readed=='yes'?'active':''}"
                                    href='#/admin/profile/notify-message?readed=yes' data-path='已读公告信息列表'>已读</a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group hide">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_title_OR_message]" class="form-control input-large"
                                   placeholder="标题，内容...">
                        </div>
                    </div>
                    <div class="form-group search-group-btn hide">
                        <button class="btn green" type="submit">
                            <i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
                        </button>
                        <button class="btn default" type="reset">
                            <i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="search-group-target"></div>
        </div>
    </div>
</div>