layui.use('element', function () {
    var element = layui.element;
    var $ = layui.jquery;

    element.on('nav(nav_bar)', function (data) {
        console.info(data.text());
        if (data.text() == "服务管理") {
            $("#iframe").attr("src", ctx + "/page/server.jsp");
        } else if (data.text() == "SQL管理") {
            $("#iframe").attr("src", ctx + "/page/sql.jsp");
        }
    });
});