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

    <title>招领公告详情</title>
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
                        window.location.href = "/lost.jsp";
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
                        <li class="active"><a href="/lost.jsp">招领</a></li>
                        <li><a href="/liuyanqiang.jsp">留言墙</a></li>
                        <li><a href="/zhandian.jsp">站点信息</a></li>
                        <li><a href="/pingtaizhinan.jsp">平台指南</a></li>
                    </ul>
                    <form action="/lost/search" method="post">
                        <div class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                                <input type="text" name="wupinmingcheng" class="form-control" placeholder="仅搜索招领">
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

<div class="container" style="margin-top: 100px;">
    <div style="background-color: #f5f5f5;border-radius: 5px;">
        <ul class="breadcrumb" style="margin-bottom: 5px">
            <li><a href="/index.jsp" style="color:#5A5A5A;">社区线上失物招领系统</a></li>
            <li><a href="/lost.jsp" style="color:#5A5A5A;">招领公告</a></li>
            <li class="active"><a style="color:#5A5A5A;">公告详情</a></li>
        </ul>
    </div>
    <div class="row" style="padding:0 15px;">
        <div class="row featurette" style="border-radius: 5px;background: #ffffff;margin-right: 0px;margin-left: 0px;">
            <%--模态框--%>
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title" id="myModalLabel">上传申领信息</h4>
                        </div>
                        <div class="modal-body">
                            <div>
                                <div class="form-group">
                                    <label for="lianxidianhua">联系电话：</label>
                                    <input type="text" name="lianxidianhua" id="lianxidianhua"/>
                                    <label for="daozhandianriqi">&nbsp;&nbsp;&nbsp;预约领取日期：</label>
                                    <input type="date" name="daozhandianriqi" id="daozhandianriqi"/>
                                </div>
                            </div>
                            <script>
                                $(function () {
                                    $("#btnupload").click(function () {
                                        uploadSL();
                                    });

                                    //验证字符串是否是数字
                                    function checkNumber(theObj) {
                                        var reg = /^[0-9]+.?[0-9]*$/;
                                        if (reg.test(theObj)) {
                                            return true;
                                        }
                                        return false;
                                    }

                                    //获取url参数的值
                                    function getQueryVariable(variable) {
                                        var query = window.location.search.substring(1);
                                        var vars = query.split("&");
                                        for (var i = 0; i < vars.length; i++) {
                                            var pair = vars[i].split("=");
                                            if (pair[0] == variable) {
                                                return pair[1];
                                            }
                                        }
                                        return (false);
                                    }

                                    function uploadSL() {
                                        var userid = "";
                                        if (document.getElementById("userid")) {
                                            //存在
                                            userid = document.getElementById('userid').innerText;
                                            userid = userid.substring(5);
                                            userid = userid.replace(/\s*/g, "");
                                        }
                                        var lianxidianhua = $("#lianxidianhua").val();
                                        var daozhandianriqi = $("#daozhandianriqi").val();
                                        var id = getQueryVariable("id");
                                        // console.log(userid, lianxidianhua, daozhandianriqi, id);
                                        if (userid.length == 0) {
                                            alert("用户信息已失效！请重新登录");
                                            return false;
                                        }
                                        if (!checkNumber(lianxidianhua) || lianxidianhua.length != 11) {
                                            alert("请填写正确的电话号码！");
                                            return false;
                                        }
                                        if (daozhandianriqi.length == 0) {
                                            alert("请选择到站点日期！");
                                            return false;
                                        }
                                        var myDate = new Date();
                                        var now = myDate.valueOf();
                                        var time = new Date(daozhandianriqi).valueOf();
                                        if(now>time){
                                            alert("请选择至少大于当前一天的日期！");
                                            return false;
                                        }
                                        var param = {
                                            "userid": userid,
                                            "id": id,
                                            "daozhandianriqi": daozhandianriqi,
                                            "lianxidianhua": lianxidianhua,
                                            "zhuangtai": "申领中"
                                        };
                                          $.ajax({
                                              url: "/lost/SLSH",
                                              type: "post",
                                              data: JSON.stringify(param),
                                              dataType: "json",
                                              contentType: "application/json;charset=UTF-8",
                                              //成功回调
                                              success: function (data) {
                                                  if (data == "succeful") {
                                                      alert("申领成功，请带好证明材料如约到站点认领！");
                                                  } else if (data == "0") {
                                                      alert("请不要重复申领！");
                                                  }else {
                                                      alert("申领失败！");
                                                  }
                                                  // data = $.parseJSON(data);
                                              },
                                              //超时时间
                                              timeout: 10000,
                                              //失败的回调
                                              error: function () {
                                                  console.log("出错啦！");
                                              }
                                          });
                                    }
                                })

                            </script>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-primary" id="btnupload">提交</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div>


            <div class="col-md-5">
                <br>
                <a class="thumbnail">
                    <img src="${dbWupin.tupian}" class="featurette-image img-responsive center-block"
                         alt="物品缩略图">
                </a>
            </div>
            <div class="col-md-7">
                <div style="margin-left:20px;margin-right:20px;">
                    <h2>${dbWupin.wupinmingcheng}
                        <a type="button" class="btn btn-default" data-toggle="modal" data-target="#myModal"
                           style="display: inline-block;float: right;margin: 6px 15px 0;">申领
                        </a>
                    </h2>
                    <p class="lead">物品状态：${dbWupin.zhuangtai}</p>
                    <p class="lead">存放站点：${dbWupin.zhandianmingcheng}（地址：${dbWupin.dizhi}）</p>
                    <p class="lead">站点负责人：${dbWupin.fuzeren}</p>
                    <p class="lead">联系电话：${dbWupin.lianxidianhua}</p>
                    <p class="lead">到站点日期：
                        <fmt:formatDate value="${dbWupin.daozhandianriqi}" pattern="yyyy-MM-dd"/>
                    </p>
                    <p class="lead">捡到日期：
                        <fmt:formatDate value="${dbWupin.jiandaoriqi}" pattern="yyyy-MM-dd"/>
                    </p>
                    <p class="lead">捡到地址：${dbWupin.jiandaodizhi}</p>
                    <p class="lead">简述：${dbWupin.jianshu}</p>
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
