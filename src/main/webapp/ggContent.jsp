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

    <title>公告详情</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script>
        $(function () {
            $("#logout").click(function () {
                logOut();
            });
        })
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
                    <form action="/lost/search" method="post">
                        <div class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                                <input type="text" name="wupinmingcheng" class="form-control" placeholder="搜索寻物和招领">
                                <button type="submit" class="btn btn-default">搜索</button>
                            </div>
                        </div>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${loginUser!=null and loginUser.username.length()>0}">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                       aria-haspopup="true" aria-expanded="false" id="username">你好，${loginUser.username}
                                        <span
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

<div class="container" style="padding:0 28px;margin-top: 100px;">
    <div class="row" style="background: #ffffff;border-radius: 5px;">
        <div class="content-wrapper__router">
            <ul class="breadcrumb">
                <li><a href="/index.jsp" style="color:#5A5A5A;">社区线上失物招领系统</a></li>
                <li class="active"><a style="color:#5A5A5A;">详情</a></li>
            </ul>
        </div>
        <div class="row featurette">
            <div style="margin-left:20px;margin-right:20px;">
                <h2 style="text-align: center">${dbNotice.g_title}</h2>
                <p style="text-align: center">发布日期：
                    <fmt:formatDate value="${dbNotice.g_addTime}" pattern="yyyy-MM-dd"/>
                </p>
                <p class="lead"style="text-align: left;text-indent:2em;">${dbNotice.g_content}</p>
                <p class="lead" style="text-align: right">发布人：${dbNotice.g_username}</p>
            </div>
        </div>
    </div>

</div>
</div>

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
