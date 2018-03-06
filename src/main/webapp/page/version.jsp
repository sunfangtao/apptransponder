<!DOCTYPE html>
<%@ page pageEncoding="UTF8" %>
<%@ include file="../common/lib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css" media="all">
</head>
<body>

<button class="layui-btn layui-btn-small" onclick="addVersion()">
    <i class="layui-icon">&#xe608;</i> 添加
</button>

<div id="version_talbe" lay-filter="versionTable"></div>

<form id="edit_version" style="display:none;padding: 5px;" class="layui-form layui-form-pane">
    <div class="layui-form-item" style="display:none;">
        <label class="layui-form-label">ID</label>
        <div class="layui-input-block">
            <input id="versionId" class="layui-input" type="label" name="id" autocomplete="off">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">所属服务</label>
        <div class="layui-input-block">
            <select id="version_type" name="serverId" lay-filter="version_type">
                <option value="">请选择服务</option>
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">版本号</label>
        <div class="layui-input-block">
            <input id="version_code" class="layui-input" type="text" name="versionCode" lay-verify="required" autocomplete="off"
                   placeholder="请输入版本号">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">资源地址</label>
        <div class="layui-input-block">
            <input id="version_url" class="layui-input" type="text" name="versionUrl" lay-verify="required" autocomplete="off"
                   placeholder="请输入地址">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">标题</label>
        <div class="layui-input-block">
            <input id="title" class="layui-input" type="text" name="title" lay-verify="required" autocomplete="off"
                   placeholder="请输入标题">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">内容</label>
        <div class="layui-input-block">
            <input id="content" class="layui-input" type="text" name="content" lay-verify="required" autocomplete="off"
                   placeholder="请输入内容">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">是否强制更新</label>
        <div class="layui-input-block">
            <input id="is_force" class="layui-input" type="text" name="isForce" lay-verify="required" autocomplete="off"
                   placeholder="1表示强制更新">
        </div>
    </div>

</form>

<script type="text/html" id="barTool">
    <a class="layui-btn layui-btn-mini" lay-event="edit">编辑</a>
</script>

<script src="${ctx}/static/layui/layui.js" charset="utf-8"></script>

<script>
    layui.use(['table', 'layer', 'form'], function () {
        var table = layui.table;
        var $ = layui.jquery;
        var form = layui.form;

        // 执行渲染
        table.render({
            id: 'version_talbe',
            elem: '#version_talbe', // 指定原始表格元素选择器（推荐id选择器）
            height: 700, // 容器高度
            cols: [[
//                {field: 'id', title: 'ID', width: 150, align: 'center'},
//                {field: 'serverId', title: '服务ID', width: 200, align: 'center'},
                {field: 'serverName', title: '服务名称', width: 200, align: 'center'},
                {field: 'versionUrl', title: '资源地址', width: 300, align: 'center'},
                {field: 'versionCode', title: '版本号', width: 100, align: 'center'},
                {field: 'title', title: '标题', width: 180, align: 'center'},
                {field: 'content', title: '更新内容', width: 300, align: 'center'},
                {field: 'isForce', title: '是否强制更新', width: 120, align: 'center'},
                {fixed: 'right', width: 80, align: 'center', toolbar: '#barTool'}
            ]], // 设置表头
            request: {
                pageName: 'page', // 页码的参数名称，默认：page
                limitName: 'pageSize' // 每页数据量的参数名，默认：limit
            },
            limit: 15,
            even: true,
            page: true,
            limits: [10, 15, 20],
            url: ctx + '/version/get',
        });

        table.on('tool(versionTable)', function (obj) {
            // 编辑模块
            editVersion(obj);
        });

        selectionServerInit();
    });

</script>

<script src="${ctx}/static/js/version.js" charset="utf-8"></script>
</body>
</html>