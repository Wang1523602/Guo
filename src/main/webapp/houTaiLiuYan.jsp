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

    <title>后台留言管理</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/me.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            LoadList();
            $("#logout").click(function () {
                logOut();
            });
        });

        function LoadList() {
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
            //总记录数
            var totalCount = 0;
            $('#totalCount').text("总记录数：" + (totalCount));
            //重置标题列表
            var liArrays = $("tbody").children();
            for (i = 0; i < liArrays.length; i++) {
                liArrays[i].remove();
            }
            // 异步请求
            $.ajax({
                url: "/liuyan/HTLY",
                type: "get",
                // data: JSON.stringify(param),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    // console.log(data)
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
                                '                        <td>' + listEntity.liuyanren + '</td>\n' +
                                '                        <td>' + listEntity.liuyanriqi + '</td>\n' +
                                '                        <td align="left" title="' + listEntity.liuyanneirong + '"  style="max-width: 100px;overflow: hidden; text-overflow:ellipsis;white-space: nowrap">' + listEntity.liuyanneirong + '</td>\n' +
                                '                        <td>\n' +
                                '                            <div class="btn-group">\n' +
                                '                                <a href="/liuyan/HTLYDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                '                            </div>\n' +
                                '                        </td>\n' +
                                '                    </tr>';
                        });
                        $('tbody[id=table1]').append(table);
                        $('#totalCount').text("总记录数：" + (totalCount + 1));
                    } else {
                        // console.log("false");
                        alert("暂无留言！");
                    }

                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("留言列表出错啦！")
                }
            });

            // console.log(wupinmingcheng, zhuangtai);

        }

        function deleteConfirm() {
            if (window.confirm("你确定要删除吗？")) {
                return true;
            }
            else {
                return false;
            }
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
                    // console.log(status);
                    // console.log(typeof status);
                    // status = status.replaceAll(" ", "");
                    if (status == 200) {
                        window.location.href = "/login.jsp";
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
                    <a class="navbar-brand" href="/houTaiIndex.jsp">社区线上失物招领系统后台管理</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li><a href="/houTaiIndex.jsp">物品管理</a></li>
                        <li class="active"><a href="/houTaiLiuYan.jsp">留言管理</a></li>
                        <li><a href="/houTaiZhanDian.jsp">站点管理</a></li>
                        <li><a href="/houTaiGongGao.jsp">公告管理</a></li>
                        <c:choose>
                            <c:when test="${loginUser!=null and loginUser.authority=='超级管理员'}">
                                <li><a href="/houTaiUser.jsp">用户权限管理</a></li>
                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${loginUser!=null and loginUser.username.length()>0}">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                       aria-haspopup="true" aria-expanded="false" id="username">你好，${loginUser.username}
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a><span id="userid">用户id：${loginUser.id}</span></a></li>
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


<div class="container" style="padding:0 28px;margin-top: 100px;">
    <div class="row" style="background: #ffffff;border-radius: 10px;">
        <ol class="breadcrumb">
            <li class="active">留言信息管理</li>
        </ol>
        <div class="pre-scrollable">
        <div class="table-responsive" style="background-color: #ffffff">
            <table class="table table-striped ">
                <thead>
                <tr>
                    <th>留言人</th>
                    <th>留言日期</th>
                    <th>留言内容</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="table1">

                </tbody>
            </table>
        </div>
        </div>
    </div>
    <ul class="pagination" style="float: right;">
        <span id="totalCount" style="color: #ffffff"></span>
    </ul>
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
