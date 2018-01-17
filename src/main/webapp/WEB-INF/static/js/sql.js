/**
 * 初始化模块下拉
 */
function selectionTypeInit() {
    var $ = layui.jquery;

    $.ajax({
        type: 'get',
        url: ctx + "/sql/getType",
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
            var typeArr = data.data.array;
            for (var i = 0; i < typeArr.length; i++) {
                var type = typeArr[i];
                $("#type_type").append("<option value=\"" + type.id + "\">" + type.name + "</option>");
            }
            var form = layui.form;
            form.render('select');
        }
    });
}

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
                $("#sql_type").append("<option value=\"" + server.id + "\">" + server.name + "</option>");
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
    table.reload('sql_talbe', {
        limit: 15,
        even: true,
        page: true,
    });
}

/**
 * 重置角色信息表单
 */
function resetForm() {
    layui.jquery('#edit_sql')[0].reset();
}

/**
 * 编辑
 * @param obj
 */
function editSql(obj) {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "编辑",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['保存'],
        area: ['400px'],
        content: $("#edit_sql"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/sql/update',
                data: $("#edit_sql").serialize(),
                success: function (data) {
                    if (data.result == "success") {
                        // 同步更新缓存对应的值
                        obj.update({
                            sql: $("#sql").val(),
                            serverId: $("#sql_type").val(),
                            serverName: $("#sql_type").find("option:selected").text(),
                            typeId: $("#type_type").val(),
                            typeName: $("#type_type").find("option:selected").text()
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
            $("#sqlId").val(obj.data.id);
            $("#sql").val(obj.data.sql);
            $("#sql_type").val(obj.data.serverId);
            $("#type_type").val(obj.data.typeId);
        }
    });
}

/**
 * 新增模块
 */
function addSql() {
    var $ = layui.jquery;
    resetForm();
    layer.open({
        title: "新建",
        type: 1,
        skin: 'layui-layer-rim',
        shadeClose: true,
        btn: ['新增'],
        area: ['400px'],
        content: $("#edit_sql"),
        yes: function (index) {
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: ctx + '/sql/add',
                data: {
                    serverId: $("#sql_type").val(),
                    typeId: $("#type_type").val(),
                    sql: $("#sql").val(),
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