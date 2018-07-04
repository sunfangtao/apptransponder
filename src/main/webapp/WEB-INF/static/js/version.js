/**
 * 初始化模块下拉
 */
function selectionServerInit() {
    var $ = layui.jquery;

    $.ajax({
        type: 'get',
        url: ctx + "/server/get",
        async: false,
        dataType: 'json',
        data: {
            page: 1,
            pageSize: 1000
        },
        error: function (request) {
            layer.msg("获取失败!", {time: 1500});
        },
        success: function (data) {
            var serverArr = data.data;
            for (var i = 0; i < serverArr.length; i++) {
                var server = serverArr[i];
                $("#version_type").append("<option value=\"" + server.id + "\">" + server.name + "</option>");
            }
            var form = layui.form;
            form.render('select');
        }
    });
}

/**
 * 重新渲染模块表格
 */
function reloadTable() {
    var table = layui.table;
    var $ = layui.jquery;
    table.reload('version_talbe', {
        limit: 15,
        even: true,
        page: true,
    });
}

/**
 * 重置角色信息表单
 */
function resetForm() {
    layui.jquery('#edit_version')[0].reset();
}

/**
 * 下载二维码图片，扫描可下载APP（仅限Android）
 */
function download(obj) {
    var $ = layui.jquery;
    $.ajax({
        type: 'post',
        url: ctx + '/version/getDownload',
        data: {
            "serverId": obj.data.serverId,
        },
        success: function (data) {
            location.href = data;
        },
        error: function (request) {
            layer.msg("下载失败!", {time: 1500});
        }
    });
}

/**
 * 编辑
 * @param obj
 */
function editVersion(obj) {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "编辑",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['保存'],
        area: ['400px'],
        content: $("#edit_version"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/version/update',
                data: $("#edit_version").serialize(),
                success: function (data) {
                    if (data.result == "success") {
                        // 同步更新缓存对应的值
                        obj.update({
                            serverId: $("#version_type").val(),
                            serverName: $("#version_type").find("option:selected").text(),
                            versionUrl: $("#version_url").val(),
                            versionCode: $("#version_code").val(),
                            title: $("#title").val(),
                            content: $("#content").val(),
                            isForce: $("#is_force").val(),
                        });
                        layer.close(index);
                        layer.msg(data.data.info, {time: 800});
                    } else {
                        layer.msg(data.message, {time: 1500});
                    }
                },
                error: function (request) {
                    layer.msg("修改失败!", {time: 1500});
                }
            });
        },
        success: function (layero, index) {
            $("#versionId").val(obj.data.id);
            $("#version_type").val(obj.data.serverId);
            $("#version_url").val(obj.data.versionUrl);
            $("#version_code").val(obj.data.versionCode);
            $("#title").val(obj.data.title);
            $("#content").val(obj.data.content);
            $("#is_force").val(obj.data.isForce);
        }
    });
}

/**
 * 新增模块
 */
function addVersion() {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "新建",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['新增'],
        area: ['400px'],
        content: $("#edit_version"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/version/add',
                data: {
                    serverId: $("#version_type").val(),
                    versionUrl: $("#version_url").val(),
                    versionCode: $("#version_code").val(),
                    title: $("#title").val(),
                    content: $("#content").val(),
                    isForce: $("#is_force").val(),
                },
                success: function (data) {
                    if (data.result == "success") {
                        // 刷新表格
                        reloadTable();
                        layer.close(index);
                        layer.msg(data.data.info, {time: 800});
                    } else {
                        layer.msg(data.message, {time: 1500});
                    }
                },
                error: function (request) {
                    layer.msg("新增失败!", {time: 1500});
                }
            });
        }
    });
}