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

    <title>社区线上失物招领系统首页</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            loadGonggao();
            loadZhaoLing();
            loadXunWu();
            $("#logout").click(function () {
                logOut();
            });

        });

        function loadGonggao() {
            $.ajax({
                url: "/swzl/notice",
                type: "get",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    let div = '';
                    data = data.replaceAll("<br/>", "\\r\\n");
                    $.each($.parseJSON(data), function (index, noticeEntity) {
                        // console.log(index, noticeEntity);
                        if (index == 0) {
                            div += '  <div class="item active">\n' +
                                '    <img class="first-slide"\n' +
                                '    src="/images/00001.webp"\n' +
                                '    alt="First slide">\n' +
                                '    <div class="container">\n' +
                                '    <div class="carousel-caption">\n' +
                                '    <h1>' + noticeEntity.g_title + '</h1>\n' +
                                '    <p>' + noticeEntity.g_content + '</p>\n' +
                                '    <p><a class="btn btn-lg btn-primary" href="/swzl/ggContent?id=' + noticeEntity.g_id + '" role="button">查看更多</a></p>\n' +
                                '    </div>\n' +
                                '    </div>\n' +
                                '    </div>';
                        }
                        if (index == 1) {
                            div += '   <div class="item">\n' +
                                '    <img class="second-slide"\n' +
                                '    src="/images/00001.webp"\n' +
                                '    alt="Second slide">\n' +
                                '    <div class="container">\n' +
                                '    <div class="carousel-caption">\n' +
                                '    <h1>' + noticeEntity.g_title + '</h1>\n' +
                                '    <p>' + noticeEntity.g_content + '</p>\n' +
                                '    <p><a class="btn btn-lg btn-primary" href="/swzl/ggContent?id=' + noticeEntity.g_id + '" role="button">查看更多</a></p>\n' +
                                '    </div>\n' +
                                '    </div>\n' +
                                '    </div>';
                        }
                        if (index == 2) {
                            div += ' <div class="item">\n' +
                                '                <img class="third-slide"\n' +
                                '                     src="/images/00001.webp"\n' +
                                '                     alt="Third slide">\n' +
                                '                <div class="container">\n' +
                                '                    <div class="carousel-caption">\n' +
                                '                            <h1>' + noticeEntity.g_title + '</h1>\n' +
                                '                            <p>' + noticeEntity.g_content + '</p>\n' +
                                '                            <p><a class="btn btn-lg btn-primary" href="/swzl/ggContent?id=' + noticeEntity.g_id + '" role="button">查看更多</a></p>\n' +

                                '                    </div>\n' +
                                '                </div>\n' +
                                '            </div>';
                        }
                    })
                    $('div[class=carousel-inner]').append(div);
                    // console.log(div);
                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("出错啦！")
                }
            });
        }

        function loadZhaoLing() {
            $.ajax({
                url: "/swzl/zhaoling",
                type: "get",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    let div = '';
                    data = data.replaceAll("<br/>", "\\r\\n");
                    $.each($.parseJSON(data), function (index, wuPinEntity) {
                        // console.log(index, noticeEntity);
                        div += ' <div class="col-lg-4">\n' +
                            '            <a href="http://localhost:8080/lost/content?id=' + wuPinEntity.id + '">  <img class="img-circle"\n' +
                            '                 src="' + wuPinEntity.tupian + '"\n' +
                            '                 alt="Generic placeholder image" width="140" height="140"></a>\n' +
                            '            <a style="color:#5A5A5A;" href="http://localhost:8080/lost/content?id=' + wuPinEntity.id + '"><h2>' + wuPinEntity.title + '（' + wuPinEntity.zhuangtai + '）</h2></a>\n' +
                            '            <p>' + wuPinEntity.jianshu + '</p>\n' +
                            '        </div>';
                    })
                    $('div[class=row]').append(div);
                    // console.log(div);
                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("出错啦！")
                }
            });
        }

        function loadXunWu() {
            $.ajax({
                url: "/swzl/xunwu",
                type: "get",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    let div = '';
                    data = data.replaceAll("<br/>", "\\r\\n");
                    $.each($.parseJSON(data), function (index, wuPinEntity) {
                        // console.log(index, wuPinEntity);
                        if (index % 2 == 0) {
                            div += '  <div class="row featurette">\n' +
                                '        <div class="col-md-7">\n' +
                                '            <h2 class="featurette-heading"><a style="color:#5A5A5A;" href="http://localhost:8080/found/content?id=' + wuPinEntity.id + '">' + wuPinEntity.title + ' （' + wuPinEntity.zhuangtai + '）</a></h2>\n' +
                                '            <p class="lead">' + wuPinEntity.jianshu + '</p>\n' +
                                '        </div>\n' +
                                '        <div class="col-md-5">\n' +
                                '            <a class="thumbnail" href="http://localhost:8080/found/content?id=' + wuPinEntity.id + '"> <img src="' + wuPinEntity.tupian + '" class="featurette-image img-responsive center-block" \n' +
                                '                 alt="Generic placeholder image"></a>\n' +
                                '        </div>\n' +
                                '    </div>\n' +
                                '\n' +
                                '    <hr class="featurette-divider">';
                        }
                        if (index % 2 == 1) {
                            div += '  <div class="row featurette">\n' +
                                '        <div class="col-md-7 col-md-push-5">\n' +
                                '            <h2 class="featurette-heading"><a style="color:#5A5A5A;" href="http://localhost:8080/found/content?id=' + wuPinEntity.id + '">' + wuPinEntity.title + '（' + wuPinEntity.zhuangtai + '） </a>\n' +
                                '            </h2>\n' +
                                '            <p class="lead">' + wuPinEntity.jianshu + '</p>\n' +
                                '        </div>\n' +
                                '        <div class="col-md-5 col-md-pull-7">\n' +
                                '            <a class="thumbnail" href="http://localhost:8080/found/content?id=' + wuPinEntity.id + '"> <img src="' + wuPinEntity.tupian + '" class="featurette-image img-responsive center-block"\n' +
                                '                 alt="Generic placeholder image"></a>\n' +
                                '        </div>\n' +
                                '    </div>\n' +
                                '\n' +
                                '    <hr class="featurette-divider">';

                        }
                    })
                    $('hr[class=featurette-divider]').after(div);
                    // console.log(div);
                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("出错啦！")
                }
            });
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

<body>
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
                        <li class="active"><a href="/index.jsp">首页</a></li>
                        <li><a href="/found.jsp">寻物</a></li>
                        <li><a href="/lost.jsp">招领</a></li>
                        <li><a href="/liuyanqiang.jsp">留言墙</a></li>
                        <li><a href="/zhandian.jsp">站点信息</a></li>
                        <li><a href="/pingtaizhinan.jsp">平台指南</a></li>
                    </ul>
                    <form action="/swzl/search" method="post">
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
                                    <a  class="dropdown-toggle" data-toggle="dropdown" role="button"
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


<!-- Carousel js拼接显示公告
================================================== -->
<div id="myCarousel" class="carousel slide" data-ride="carousel">
    <div class="carousel-inner" role="listbox">

    </div>
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>


<!-- Wrap the rest of the page in another container to center all the content. -->

<div class="container marketing">

    <!-- Three columns of text below the carousel -->
    <div class="row">

    </div><!-- /.row -->


    <!-- START THE FEATURETTES -->

    <hr class="featurette-divider">


    <!-- /END THE FEATURETTES -->


    <!-- FOOTER -->
    <footer>
        <p class="pull-right"><a href="#">返回顶部</a></p>
        <p>&copy; 2025 郭怡彤</p>
    </footer>

</div><!-- /.container -->
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
