<!DOCTYPE html>
<%@ page pageEncoding="UTF8" %>
<%@ include file="../common/lib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css" media="all">
</head>
<body>

<button class="layui-btn layui-btn-small" onclick="addServer()">
    <i class="layui-icon">&#xe608;</i> 添加
</button>

<div id="server_talbe" lay-filter="serverTable"></div>

<form id="edit_server" style="display:none;padding: 5px;" class="layui-form layui-form-pane">
    <div class="layui-form-item" style="display:none;">
        <label class="layui-form-label">服务ID</label>
        <div class="layui-input-block">
            <input id="serverId" class="layui-input" type="label" name="id" autocomplete="off">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">名称</label>
        <div class="layui-input-block">
            <input id="name" class="layui-input" type="text" name="name" lay-verify="required" autocomplete="off"
                   placeholder="请输入名称">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">地址</label>
        <div class="layui-input-block">
            <input id="address" class="layui-input" type="text" name="address" lay-verify="required" autocomplete="off"
                   placeholder="请输入地址">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">IP</label>
        <div class="layui-input-block">
            <input id="ip" class="layui-input" type="text" name="ip" lay-verify="required" autocomplete="off"
                   placeholder="请输入IP">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">Port</label>
        <div class="layui-input-block">
            <input id="port" class="layui-input" type="text" name="port" lay-verify="required" autocomplete="off"
                   placeholder="请输入port">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">数据库名称</label>
        <div class="layui-input-block">
            <input id="dbname" class="layui-input" type="text" name="dbName" lay-verify="required" autocomplete="off"
                   placeholder="请输入数据库名称">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block">
            <input id="username" class="layui-input" type="text" name="userName" lay-verify="required"
                   autocomplete="off"
                   placeholder="请输入用户名">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block">
            <input id="password" class="layui-input" type="text" name="password" lay-verify="required"
                   autocomplete="off"
                   placeholder="请输入密码">
        </div>
    </div>
</form>

<script type="text/html" id="barTool">
    <a class="layui-btn layui-btn-mini" lay-event="edit">编辑</a>
</script>

<script type="text/html" id="dateTpl">
    {{ d.createTime.split('.')[0] }}
</script>

<script src="${ctx}/static/layui/layui.js" charset="utf-8"></script>

<script>
    layui.use(['table', 'layer', 'form'], function () {
        var table = layui.table;
        var $ = layui.jquery;
        var form = layui.form;

        // 执行渲染
        table.render({
            id: 'server_talbe',
            elem: '#server_talbe', // 指定原始表格元素选择器（推荐id选择器）
            height: 700, // 容器高度
            cols: [[
                // {field: 'id', title: 'ID', width: 150, align: 'center'},
                {field: 'name', title: '名称', width: 150, align: 'center'},
                {field: 'ip', title: '数据库地址', width: 200, align: 'center'},
                {field: 'port', title: '端口', width: 80, align: 'center'},
                {field: 'dbName', title: '数据库名称', width: 100, align: 'center'},
                {field: 'userName', title: '用户名', width: 100, align: 'center'},
                {field: 'password', title: '密码', width: 200, align: 'center'},
                {field: 'address', title: '访问地址', width: 400, align: 'center'},
                {field: 'createTime', title: '创建日期', width: 200, align: 'center', templet: '#dateTpl'},
                {fixed: 'right', width: 200, align: 'center', toolbar: '#barTool'}
            ]], // 设置表头
            request: {
                pageName: 'page', // 页码的参数名称，默认：page
                limitName: 'pageSize' // 每页数据量的参数名，默认：limit
            },
            limit: 15,
            even: true,
            page: true,
            limits: [10, 15, 20],
            url: ctx + '/server/get',
        });

        table.on('tool(serverTable)', function (obj) {
            // 编辑模块
            editServer(obj);
        });
    });
</script>

<script src="${ctx}/static/js/server.js" charset="utf-8"></script>
</body>
</html>