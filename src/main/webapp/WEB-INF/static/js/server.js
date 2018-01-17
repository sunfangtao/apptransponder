/**
 * 重新渲染模块表格
 */
function reloadTable() {
    var table = layui.table;
    var $ = layui.jquery;
    table.reload('server_talbe', {
        limit: 15,
        even: true,
        page: true,
    });
}

/**
 * 重置角色信息表单
 */
function resetForm() {
    layui.jquery('#edit_server')[0].reset();
}

/**
 * 编辑
 * @param obj
 */
function editServer(obj) {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "编辑",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['保存'],
        area: ['400px'],
        content: $("#edit_server"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/server/update',
                data: $("#edit_server").serialize(),
                success: function (data) {
                    if (data.result == "success") {
                        // 同步更新缓存对应的值
                        obj.update({
                            name: $("#name").val(),
                            ip: $("#ip").val(),
                            port: $("#port").val(),
                            dbName: $("#dbname").val(),
                            userName: $("#username").val(),
                            password: $("#password").val(),
                            address: $("#address").val(),
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
            $("#name").val(obj.data.name);
            $("#serverId").val(obj.data.id);
            $("#ip").val(obj.data.ip);
            $("#port").val(obj.data.port);
            $("#dbname").val(obj.data.dbName);
            $("#username").val(obj.data.userName);
            $("#password").val(obj.data.password);
            $("#address").val(obj.data.address);
        }
    });
}

/**
 * 新增模块
 */
function addServer() {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "新建",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['新增'],
        area: ['400px'],
        content: $("#edit_server"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/server/add',
                data: $("#edit_server").serialize(),
                success: function (data) {
                    if (data.result == "success") {
                        // 刷新表格
                        reloadTable();
                        layer.close(index);
                        layer.msg("操作成功", {time: 800});
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