<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/images/swzl.ico">

    <title>个人中心</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/me.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            // LoadList();
            // console.log(zhuangtai);
            var zhuangtai = sessionStorage.getItem('SLzhuangtai');
            // console.log(zhuangtai);
            if (zhuangtai == null) {
                zhuangtai = 1;
            }
            LoadList(zhuangtai);
            // console.log(zhuangtai);
            $("#update").click(function () {
                zhuangtai = $("#zhuangtai").val();
                LoadList(zhuangtai);
            });
            $("#logout").click(function () {
                logOut();
            });
        });

        function deleteConfirm() {
            if (window.confirm("你确定要删除吗？")) {
                return true;
            }
            else {
                return false;
            }
        }

        function LoadList(zhuangtai) {
            var userid = "";
            if (document.getElementById("userid")) {
                userid = document.getElementById('userid').innerText;
                userid = userid.substring(5);
                userid = userid.replace(/\s*/g, "");
            }
            if (userid.length == 0) {
                alert("登录已过期！");
                return false;
            }
            var param = {"userid": userid};
            sessionStorage.setItem('SLzhuangtai', zhuangtai);
            //重置标题列表
            var liArrays = $("tbody").children();
            for (i = 0; i < liArrays.length; i++) {
                liArrays[i].remove();
            }
            var totalCount = 0;
            $('#totalCount').text("总记录数：" + (totalCount));
            if (zhuangtai == 1){
                // 异步请求
                $.ajax({
                    url: "/lost/userSLSH",
                    type: "post",
                    data: JSON.stringify(param),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    //成功回调
                    success: function (data) {
                        console.log(data)
                        data = $.parseJSON(data);
                        var status = data.status;
                        status = status.replaceAll(" ", "");
                        // console.log(status)
                        if (status == 'true') {
                            let table = '';
                            data = data.list;
                            $.each(data, function (index, listEntity) {
                                totalCount = index;
                                table += '<tr>\n' +
                                    '                        <td>\n' +
                                    '                            <img src="' + listEntity.tupian + '" class="img-thumbnail" style="height: 30px;"/>\n' +
                                    '</td>\n' +
                                    '                        <td>' + listEntity.wupinmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.daozhandianriqi + '</td>\n' +
                                    '                        <td>' + listEntity.zhandianmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.lianxidianhua + '</td>\n' +
                                    '                        <td align="left" title="\' + listEntity.liuyanneirong + \'"  style="max-width: 100px;overflow: hidden; text-overflow:ellipsis;white-space: nowrap">' + listEntity.dizhi + '</td>\n' +
                                    '                        <td>' + listEntity.zhuangtai + '（' + listEntity.shenhejieguo + '）</td>\n' +
                                    '                        <td>\n' +
                                    '                            <div class="btn-group">\n' +
                                    '                                <a href="/lost/SLSHContent?id=' + listEntity.id + '" class="btn btn-default">查看</a>\n' +
                                    '                                <a href="/lost/SLSHDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                    '                            </div>\n' +
                                    '                        </td>\n' +
                                    '                    </tr>';
                            });
                            $('tbody[id=table1]').append(table);
                            $('#totalCount').text("总记录数：" + (totalCount + 1));
                        } else {
                            // console.log("false");
                            alert("暂无数据！");
                        }

                    },
                    //超时时间
                    timeout: 10000,
                    //失败的回调
                    error: function () {
                        console.log("列表标题出错啦！")
                    }
                });
            }else  if (zhuangtai == 2){
                $.ajax({
                    url: "/lost/userSLJX",
                    type: "post",
                    data: JSON.stringify(param),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    //成功回调
                    success: function (data) {
                        console.log(data)
                        data = $.parseJSON(data);
                        var status = data.status;
                        status = status.replaceAll(" ", "");
                        // console.log(status)
                        if (status == 'true') {
                            let table = '';
                            data = data.list;
                            $.each(data, function (index, listEntity) {
                                totalCount = index;
                                table += '<tr>\n' +
                                    '                        <td>\n' +
                                    '                            <img src="' + listEntity.tupian + '" class="img-thumbnail" style="height: 30px;"/>\n' +
                                    '</td>\n' +
                                    '                        <td>' + listEntity.wupinmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.daozhandianriqi + '</td>\n' +
                                    '                        <td>' + listEntity.zhandianmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.lianxidianhua + '</td>\n' +
                                    '                        <td align="left" title="\' + listEntity.liuyanneirong + \'"  style="max-width: 100px;overflow: hidden; text-overflow:ellipsis;white-space: nowrap">' + listEntity.dizhi + '</td>\n' +
                                    '                        <td>' + listEntity.zhuangtai + '</td>\n' +
                                    '                        <td>\n' +
                                    '                            <div class="btn-group">\n' +
                                    '                                <a href="/lost/SLSHContent?id=' + listEntity.id + '" class="btn btn-default">查看</a>\n' +
                                    '                                <a href="/lost/SLSHDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                    '                            </div>\n' +
                                    '                        </td>\n' +
                                    '                    </tr>';
                            });
                            $('tbody[id=table1]').append(table);
                            $('#totalCount').text("总记录数：" + (totalCount + 1));
                        } else {
                            // console.log("false");
                            alert("暂无数据！");
                        }

                    },
                    //超时时间
                    timeout: 10000,
                    //失败的回调
                    error: function () {
                        console.log("列表标题出错啦！")
                    }
                });
            }else if (zhuangtai == 3){
                $.ajax({
                    url: "/lost/userSLWC",
                    type: "post",
                    data: JSON.stringify(param),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    //成功回调
                    success: function (data) {
                        console.log(data)
                        data = $.parseJSON(data);
                        var status = data.status;
                        status = status.replaceAll(" ", "");
                        // console.log(status)
                        if (status == 'true') {
                            let table = '';
                            data = data.list;
                            $.each(data, function (index, listEntity) {
                                totalCount = index;
                                table += '<tr>\n' +
                                    '                        <td>\n' +
                                    '                            <img src="' + listEntity.tupian + '" class="img-thumbnail" style="height: 30px;"/>\n' +
                                    '</td>\n' +
                                    '                        <td>' + listEntity.wupinmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.daozhandianriqi + '</td>\n' +
                                    '                        <td>' + listEntity.zhandianmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.lianxidianhua + '</td>\n' +
                                    '                        <td align="left" title="\' + listEntity.liuyanneirong + \'"  style="max-width: 100px;overflow: hidden; text-overflow:ellipsis;white-space: nowrap">' + listEntity.dizhi + '</td>\n' +
                                    '                        <td>' + listEntity.zhuangtai + '</td>\n' +
                                    '                        <td>\n' +
                                    '                            <div class="btn-group">\n' +
                                    '                                <a href="/lost/SLSHContent?id=' + listEntity.id + '" class="btn btn-default">查看</a>\n' +
                                    '                                <a href="/lost/SLSHDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                    '                            </div>\n' +
                                    '                        </td>\n' +
                                    '                    </tr>';
                            });
                            $('tbody[id=table1]').append(table);
                            $('#totalCount').text("总记录数：" + (totalCount + 1));
                        } else {
                            // console.log("false");
                            alert("暂无数据！");
                        }

                    },
                    //超时时间
                    timeout: 10000,
                    //失败的回调
                    error: function () {
                        console.log("列表标题出错啦！")
                    }
                });
            }else if (zhuangtai == 4){
                $.ajax({
                    url: "/lost/userSLJJ",
                    type: "post",
                    data: JSON.stringify(param),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    //成功回调
                    success: function (data) {
                        console.log(data)
                        data = $.parseJSON(data);
                        var status = data.status;
                        status = status.replaceAll(" ", "");
                        // console.log(status)
                        if (status == 'true') {
                            let table = '';
                            data = data.list;
                            $.each(data, function (index, listEntity) {
                                totalCount = index;
                                table += '<tr>\n' +
                                    '                        <td>\n' +
                                    '                            <img src="' + listEntity.tupian + '" class="img-thumbnail" style="height: 30px;"/>\n' +
                                    '</td>\n' +
                                    '                        <td>' + listEntity.wupinmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.daozhandianriqi + '</td>\n' +
                                    '                        <td>' + listEntity.zhandianmingcheng + '</td>\n' +
                                    '                        <td>' + listEntity.lianxidianhua + '</td>\n' +
                                    '                        <td align="left" title="\' + listEntity.liuyanneirong + \'"  style="max-width: 100px;overflow: hidden; text-overflow:ellipsis;white-space: nowrap">' + listEntity.dizhi + '</td>\n' +
                                    '                        <td>' + listEntity.zhuangtai + '（' + listEntity.shenhejieguo + '）</td>\n' +
                                    '                        <td>\n' +
                                    '                            <div class="btn-group">\n' +
                                    '                                <a href="/lost/SLSHContent?id=' + listEntity.id + '" class="btn btn-default">查看</a>\n' +
                                    '                                <a href="/lost/SLSHDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                    '                            </div>\n' +
                                    '                        </td>\n' +
                                    '                    </tr>';
                            });
                            $('tbody[id=table1]').append(table);
                            $('#totalCount').text("总记录数：" + (totalCount + 1));
                        } else {
                            // console.log("false");
                            alert("暂无数据！");
                        }

                    },
                    //超时时间
                    timeout: 10000,
                    //失败的回调
                    error: function () {
                        console.log("列表标题出错啦！")
                    }
                });
            }else{
                alert("出错啦！")
            }


            // console.log(wupinmingcheng, zhuangtai);

        }

        function logOut() {
            $.ajax({
                type: "post",
                url: "/user/logout",
                cache: false,
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (data) {
                    data = $.parseJSON(data);
                    // console.log(data);
                    var status = data.status;
                    console.log(status);
                    console.log(typeof status);
                    // status = status.replaceAll(" ", "");
                    if (status == 200) {
                        window.location.href = "/index.jsp";
                    } else {
                        alert("退出失败！");
                    }
                },
            });
        }
    </script>
</head>

<body background="../images/background.jpg" style=" background-repeat:no-repeat ;background-size:100% 100%;
background-attachment: fixed;">
<div class="navbar-wrapper">
    <div class="container">
        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#navbar"
                            aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">切换导航</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/index.jsp">社区线上失物招领系统</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="/index.jsp">首页</a></li>
                        <li><a href="/found.jsp">寻物</a></li>
                        <li><a href="/lost.jsp">招领</a></li>
                        <li><a href="/liuyanqiang.jsp">留言墙</a></li>
                        <li><a href="/zhandian.jsp">站点信息</a></li>
                        <li><a href="/pingtaizhinan.jsp">平台指南</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${loginUser!=null and loginUser.username.length()>0}">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                       aria-haspopup="true" aria-expanded="false">你好，${loginUser.username} <span
                                            class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a><span id="userid">用户id：${loginUser.id}</span></a></li>
                                        <li><a href="/userXunWu.jsp">个人中心</a></li>
                                        <li><a id="logout">注销登录</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="/login.jsp">你好，请登录！</a></li>
                            </c:otherwise>
                        </c:choose>

                    </ul>
                </div>
            </div>
        </nav>
    </div>
</div>


<div class="container" style="margin-top: 100px;">
    <div class="row">
        <div class="col-sm-2">
            <a href="/userXunWu.jsp" class="list-group-item" id="xunwubtn">
                <span class="glyphicon glyphicon-align-left"></span>寻物信息管理
            </a>
            <a href="/userZhaoLing.jsp" class="list-group-item" id="zhaolingbtn">
                        <span class="glyphicon glyphicon-align-left" aria-hidden="true">
                    </span>招领信息管理</a>
            <a href="/userLiuYan.jsp" class="list-group-item" id="liuyanbtn">
                        <span class="glyphicon glyphicon-align-left" aria-hidden="true">
                    </span>留言信息管理</a>
            <a href="/userShenLing.jsp" class="list-group-item active" id="renlingbtn"
               style="background-color: #5a5a5a;border-color: #5a5a5a;">
                        <span class="glyphicon glyphicon-align-left" aria-hidden="true">
                    </span>申领信息管理</a>
        </div>
        <!--左边菜单栏-->
        <div class="col-sm-10">
            <div style="background-color: #ffffff">
                <ol class="breadcrumb">
                    <li class="active">申领信息管理</li>
                </ol>
                <div class=" panel panel-default">
                    <div class="panel-body">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="zhuangtai">状态</label>
                                <select class="form-control" id="zhuangtai" name="zhuangtai" type="text" required>
                                    <option value="1">审核中</option>
                                    <option value="2">正在进行</option>
                                    <option value="3">已完成</option>
                                    <option value="4">未通过</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <button type="button" class="btn btn-default" id="update">更新</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--
                    列表展示
                -->
                <div class="pre-scrollable">
                    <div class="table-responsive" style="background-color: #ffffff">
                        <table class="table table-striped ">
                            <thead>
                            <tr>
                                <th>图片</th>
                                <th>物品名称</th>
                                <th>预约领取日期</th>
                                <th>站点</th>
                                <th>站点电话</th>
                                <th>站点地址</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="table1">

                            </tbody>
                        </table>
                    </div>
                </div>
                <ul class="pagination" style="float: right;">
                    <span id="totalCount" style="color: #ffffff"></span>
                </ul>
            </div>
        </div>
    </div>
</div>
<!-- 底部页脚部分 -->
<!-- FOOTER -->
<div class="container marketing">
    <br>
    <br>
    <footer>
        <p class="pull-right"><a href="#">返回顶部</a></p>
        <p>&copy; 2025 郭怡彤</p>
    </footer>
</div>
<!-- /.container -->
<!-- Bootstrap core JavaScript
================================================== -->
<!-- 放置在文档末尾，以便页面加载更快 -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"
        integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ"
        crossorigin="anonymous"></script>
<script>window.jQuery || document.write('<script src="/js/jquery.min.js"><\/script>')</script>
<script src="/js/bootstrap.min.js"></script>


</body>
</html>
