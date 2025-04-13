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

    <title>后台寻物管理详情</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/me.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            // LoadList(currPage);

            $("#logout").click(function () {
                logOut();
            });
        });


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
                        <li class="active"><a href="/houTaiIndex.jsp">物品管理</a></li>
                        <li><a href="/houTaiLiuYan.jsp">留言管理</a></li>
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


<div class="container" style="margin-top: 100px;">
    <div class="row">
        <div class="col-sm-2">
            <a href="/houTaiIndex.jsp" class="list-group-item active" id="xunwubtn"
               style="background-color: #5a5a5a;border-color: #5a5a5a;">
                <span class="glyphicon glyphicon-align-left"></span>寻物信息管理
            </a>
            <a href="/houTaiZhaoLing.jsp" class="list-group-item" id="zhaolingbtn">
                        <span class="glyphicon glyphicon-align-left" aria-hidden="true">
                    </span>招领信息管理</a>
            <a href="/houTaiShenLing.jsp" class="list-group-item" id="renlingbtn">
                        <span class="glyphicon glyphicon-align-left" aria-hidden="true">
                    </span>申领信息管理</a>
        </div>
        <!--左边菜单栏-->
        <div class="col-sm-10">
            <ol class="breadcrumb">
                <li class="active"><a href="/houTaiIndex.jsp" style="color: #5a5a5a;">寻物信息管理</a></li>
                <li class="active">详情</li>
            </ol>

            <!--
                列表展示
            -->
            <div class="pre-scrollable" style="max-height: 425px">
                <div class="table-responsive" style="background-color: #ffffff;border-radius: 5px;">
                    <div class="col-md-5" style="display: inline-block;" align="center">
                        <br>
                        <br>
                        <a class="thumbnail">
                            <img src="${dbWupin.tupian}" class="featurette-image img-responsive center-block"
                                 alt="物品缩略图">
                        </a>
                    </div>
                    <div class="col-md-7" style="display: inline-block;">
                        <div style="margin-left:20px;margin-right:20px;">
                            <h2>${dbWupin.wupinmingcheng}</h2>
                            <c:choose>
                                <c:when test="${dbWupin!=null and dbWupin.shenhejieguo.length()>0}">
                                    <p class="lead">物品状态：${dbWupin.zhuangtai}（${dbWupin.shenhejieguo}）</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="lead">物品状态：${dbWupin.zhuangtai}</p>
                                </c:otherwise>
                            </c:choose>
                            <p class="lead">期望归还站点：${dbWupin.zhandianmingcheng}（地址：${dbWupin.dizhi}）</p>
                            <p class="lead">站点负责人：${dbWupin.fuzeren}</p>
                            <p class="lead">失主：${dbWupin.shiquren}</p>
                            <p class="lead">联系电话：${dbWupin.lianxidianhua}</p>
                            <p class="lead">丢失日期：
                                <fmt:formatDate value="${dbWupin.shiquriqi}" pattern="yyyy-MM-dd"/>
                            </p>
                            <p class="lead">丢失地址：${dbWupin.shiqudizhi}</p>
                            <p class="lead">简述：${dbWupin.jianshu}</p>
                        </div>
                    </div>
                </div>
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
