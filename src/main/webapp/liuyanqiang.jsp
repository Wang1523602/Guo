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

    <title>留言墙</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/me.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        //定义全局总页数
        var totalPage = 1;
        $(document).ready(function () {
            var currPage = 1;
            loadList(currPage);
            $("#sendText").click(function () {
                uploadLiuYan();
            });
            $("#loadMore").click(function () {
                if (currPage < totalPage) {
                    currPage++;
                    loadList(currPage);
                } else {
                    alert("已经是最后一页啦！")
                }
            });
            $("#logout").click(function () {
                logOut();
            });
        });

        function uploadLiuYan() {
            var username = "";
            var userid = "";
            if (document.getElementById("username")) {
//存在
                username = document.getElementById('username').innerText;
                username = username.substring(3);
                username = username.replace(/\s*/g, "");
            }
            // console.log(username);
            if (document.getElementById("userid")) {
//存在
                userid = document.getElementById('userid').innerText;
                userid = userid.substring(5);
                userid = userid.replace(/\s*/g, "");
            }
            // console.log(userid);
            // console.log(typeof userid);
            var liuyanneirong = $("#liuyanneirong").val();
            // console.log(liuyanneirong);
            if (username.length == 0) {
                alert("请登录后再发表评论！");
                return false;
            }
            if (userid.length == 0) {
                alert("请登录后再发表评论！");
                return false;
            }
            if (liuyanneirong.length == 0) {
                alert("请填写评论再上传！");
                return false;
            }
            var param = {
                "userid": userid,
                "liuyanren": username,
                "liuyanneirong": liuyanneirong
            };
            //异步请求
            $.ajax({
                url: "/liuyan/information",
                type: "post",
                data: JSON.stringify(param),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    // console.log(data)
                    if (data == "succeful") {
                        alert("留言上传成功！")
                    } else {
                        alert("上传失败！")
                    }

                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("留言上传出错啦！")
                }
            });
            location.reload();
        }

        function loadList(currPage) {
            var param = {"currPage": currPage};
            //异步请求
            $.ajax({
                url: "/liuyan/list",
                type: "post",
                data: JSON.stringify(param),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                //成功回调
                success: function (data) {
                    data = $.parseJSON(data);
                    var status = data.status;
                    status = status.replaceAll(" ", "");
                    if (status == 'true') {
                        let list = '';
                        //总页数
                        totalPage = parseInt(data.totalPage);
                        //获取列表数据
                        data = data.list;
                        $.each(data, function (index, listEntity) {
                            list += ' <div class="p-1 comments">\n' +
                                '                        <div class=" ml-3 border-left ">\n' +
                                '                            <div class=" ml-3 border-left ">\n' +
                                '                                <span style="font-size: 15px"><a>@' + listEntity.liuyanren + '</a></span>\n' +
                                '                                <span class="summary-text small">' + listEntity.liuyanriqi + '</span>\n' +
                                '                            </div>\n' +
                                '                            <span class=" ml-3 border-left " style="font-size: 20px">' + listEntity.liuyanneirong + '</span>\n' +
                                '                        </div>\n' +

                                '                </div>\n' +
                                '                <hr/>';
                        });
                        $('#liuYanList').append(list);
                    } else {
                        alert("留言墙为空！");
                    }
                    // console.log(data)


                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("留言加载出错啦！")
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
                        window.location.href = "/liuyanqiang.jsp";
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
                        <li class="active"><a href="/liuyanqiang.jsp">留言墙</a></li>
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
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                       aria-haspopup="true" aria-expanded="false" id="username">你好，${loginUser.username}
                                        <span class="caret"></span></a>
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
    <div class="row" style="background: #ffffff;border-radius: 6px;">
        <div class="content-wrapper__router">
            <ul class="breadcrumb">
                <li class="active"><a style="color:#5A5A5A;">留言墙</a></li>
            </ul>
        </div>
        <div class="pre-scrollable" style="overflow-x: hidden;height:400px;">
            <div class=" container-fluid mt-5 border p-2" id="liuYanList" style="border-top: #5A5A5A">


            </div>
            <div align="center">
                <button id="loadMore">加载更多</button>
            </div>
        </div>
        <br>
        <div class="row featurette">
            <div style="margin-left:20px;margin-right:20px;">
                <!--            输入框-->
                <div class="form-group">
                    <textarea class="form-control" rows="3" style="font-size: 20px" id="liuyanneirong"></textarea>
                </div>
                <!--提交列表-->
                <div class="input list-inline mt-3 container-fluid ">
                    <div class="  list-inline-item col-lg-auto  p-0">
                        <button type="button" class="btn  btn-info " id="sendText"
                                style="width: 30%;height: 30%;float: right;">发表评论
                        </button>
                    </div>
                </div>
                <br/>
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
