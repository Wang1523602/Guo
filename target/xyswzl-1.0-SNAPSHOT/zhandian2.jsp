<%@ page import="com.swzl.entity.Wupin" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.swzl.entity.Zhandian" %>
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

    <title>站点搜索界面</title>
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
                        window.location.href = "/zhandian.jsp";
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
                        <li class="active"><a href="/zhandian.jsp">站点信息</a></li>
                        <li><a href="/pingtaizhinan.jsp">平台指南</a></li>
                    </ul>
                    <form action="/zhandian/search" method="post">
                        <div class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                                <input type="text" name="zhandianmingcheng" class="form-control" placeholder="仅搜索站点">
                                <button type="submit" class="btn btn-default">搜索</button>
                            </div>
                        </div>
                    </form>
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

<%
    ArrayList<Zhandian> list = (ArrayList<Zhandian>) request.getSession().getAttribute("dbZhanDian");  //此处是取出所存储的数据
    int page_current = 1; //当前页数
    int page_begin = 0;  //起始点,注意:下标从0开始
    int page_end = 9;   //终点,每页十条信息
    int total_count = 0;
    int page_total = 0;
    if (list != null) {
        total_count = list.size();   //信息的总量
        page_total = total_count / 10 + (total_count % 10 != 0 ? 1 : 0);
        if (request.getParameter("begin") != null) {
            page_current = Integer.parseInt(request.getParameter("begin"));  //获取当前页数
        }
        page_begin = (page_current - 1) * 10;
        page_end = page_begin + 9 > total_count ? total_count : page_begin + 9;
    }
    request.getSession().setAttribute("page_current", page_current);  //保存到session中
    request.getSession().setAttribute("page_total", page_total);
    request.getSession().setAttribute("total_count", total_count);

%>

<div class="container" style="margin-top: 100px">
    <div style="background-color: #f5f5f5;border-radius: 5px;">
        <ul class="breadcrumb" style="display: inline-block;margin-bottom: 5px">
            <li><a href="/index.jsp" style="color:#5A5A5A;">社区线上失物招领系统</a></li>
            <li><a href="/zhandian.jsp" style="color:#5A5A5A;">站点信息</a></li>
            <li class="active"><a style="color:#5A5A5A;">搜索结果</a></li>
        </ul>
    </div>
    <div class="row" style="padding:0 15px;">
        <ul class="list-group">

            <c:choose>
                <c:when test="${dbZhanDian!=null}">
                    <c:forEach items="${dbZhanDian}" var="res" begin="<%=page_begin %>" end="<%=page_end %>">
                        <li class="list-group-item">
                   <span class="badge"> <fmt:formatDate value="${res.addtime}" pattern="yyyy-MM-dd"></fmt:formatDate>
                    </span>
                            <span class="glyphicon glyphicon-home"></span>
                            <a style="color:#5A5A5A;"
                               href="/zhandian/content?id=${res.id}">${res.zhandianmingcheng}（地址：${res.dizhi}）</a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li class="list-group-item">
                        <span>物品不存在或关键词为空！</span>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
        <div style="text-align:center">
            <div class="pagination" style="color: #ffffff">
                <a id="firstPageId" style="color: #ffffff" href="/zhandian2.jsp?begin=1">首页</a>
                <c:if test="${sessionScope.page_current != 1 }">
                    <a id="prevPageId" style="color: #ffffff"
                       href="/zhandian2.jsp?begin=${sessionScope.page_current - 1 }">上一页</a>
                </c:if>
                <c:if test="${sessionScope.page_current != sessionScope.page_total}">
                    <a id="nextPageId" style="color: #ffffff"
                       href="/zhandian2.jsp?begin=${sessionScope.page_current + 1 }">下一页</a>
                </c:if>
                <a id="lastPageId" style="color: #ffffff" href="/zhandian2.jsp?begin=${sessionScope.page_total }">尾页</a>
                <span id="currPage">当前的页码：${sessionScope.page_current } </span>
                <span id="totalPage">总页数：${sessionScope.page_total }</span>
                <span id="totalCount">总记录数：${sessionScope.total_count }</span>
            </div>
        </div>
    </div>
</div>

<!-- FOOTER -->
<div class="container marketing">
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
