<!DOCTYPE html>
<%@ page pageEncoding="UTF8" %>
<%@ include file="../common/lib.jsp" %>

<html>
<head>
    <meta charset="utf-8">
    <title>APP管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css" media="all">
</head>
<body>

<div class="layui-form-item">
    <ul class="layui-nav" lay-filter="nav_bar">
        <li class="layui-nav-item"><a>服务管理</a></li>
        <li class="layui-nav-item"><a>SQL管理</a></li>
        <li class="layui-nav-item"><a>APP版本管理</a></li>
    </ul>
</div>

<iframe id="iframe" style="border: none" width="100%" height="800px"></iframe>

<script src="${ctx}/static/layui/layui.js" charset="utf-8"></script>
<script src="${ctx}/static/js/index.js" charset="utf-8"></script>

</body>
</html>