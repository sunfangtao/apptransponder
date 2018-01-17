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

<button class="layui-btn layui-btn-small" onclick="addSql()">
    <i class="layui-icon">&#xe608;</i> 添加
</button>

<div id="sql_talbe" lay-filter="sqlTable"></div>

<form id="edit_sql" style="display:none;padding: 5px;" class="layui-form layui-form-pane">
    <div class="layui-form-item" style="display:none;">
        <label class="layui-form-label">ID</label>
        <div class="layui-input-block">
            <input id="sqlId" class="layui-input" type="label" name="id" autocomplete="off">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">所属服务</label>
        <div class="layui-input-block">
            <select id="sql_type" name="serverId" lay-filter="sql_type">
                <option value="">请选择服务</option>
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">类型</label>
        <div class="layui-input-block">
            <select id="type_type" name="typeId" lay-filter="type_type">
                <option value="">请选择类型</option>
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">SQL</label>
        <div class="layui-input-block">
            <input id="sql" class="layui-input" type="text" name="sql" lay-verify="required" autocomplete="off"
                   placeholder="请输入SQL">
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
            id: 'sql_talbe',
            elem: '#sql_talbe', // 指定原始表格元素选择器（推荐id选择器）
            height: 700, // 容器高度
            cols: [[
                {field: 'id', title: 'ID', width: 150, align: 'center'},
//                {field: 'serverId', title: '服务ID', width: 200, align: 'center'},
                {field: 'serverName', title: '服务名称', width: 200, align: 'center'},
//                {field: 'typeId', title: '类型ID', width: 200, align: 'center'},
//                {field: 'type', title: '类型', width: 200, align: 'center'},
                {field: 'typeName', title: '类型名称', width: 300, align: 'center'},
                {field: 'sql', title: '查询命令', width: 1000, align: 'center'},
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
            url: ctx + '/sql/get',
        });

        table.on('tool(sqlTable)', function (obj) {
            // 编辑模块
            editSql(obj);
        });

        selectionServerInit();
        selectionTypeInit();
    });

</script>

<script src="${ctx}/static/js/sql.js" charset="utf-8"></script>
</body>
</html>