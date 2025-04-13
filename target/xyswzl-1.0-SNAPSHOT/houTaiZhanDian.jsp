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

    <title>后台站点管理</title>
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
                url: "/zhandian/HTZDList",
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
                                '                        <td>' + listEntity.bianhao + '</td>\n' +
                                '                        <td>' + listEntity.zhandianmingcheng + '</td>\n' +
                                '                        <td>' + listEntity.fuzeren + '</td>\n' +
                                '                        <td>' + listEntity.lianxidianhua + '</td>\n' +
                                '                        <td>' + listEntity.youxiang + '</td>\n' +
                                '                        <td>' + listEntity.fuwushijian + '</td>\n' +
                                '                        <td>\n' +
                                '                            <div class="btn-group">\n' +
                                '                                <a href="/zhandian/HTZDContent?id=' + listEntity.id + '" class="btn btn-default">查看</a>\n' +
                                '                                <a href="/zhandian/HTZDDelete?id=' + listEntity.id + '" class="btn btn-danger" onclick="return deleteConfirm()">删除</a>\n' +
                                '                            </div>\n' +
                                '                        </td>\n' +
                                '                    </tr>';
                        });
                        $('tbody[id=table1]').append(table);
                        $('#totalCount').text("总记录数：" + (totalCount + 1));
                    } else {
                        // console.log("false");
                        alert("暂无站点信息！");
                    }

                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("站点列表出错啦！")
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
                        <li><a href="/houTaiLiuYan.jsp">留言管理</a></li>
                        <li class="active"><a href="/houTaiZhanDian.jsp">站点管理</a></li>
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
        <div style="background-color: #f5f5f5;border-radius: 5px;">
            <ul class="breadcrumb" style="display: inline-block;margin-bottom: 5px">
                <li class="active">站点信息管理</li>
            </ul>
            <button type="button" data-toggle="modal" data-target="#myModal"
                    style="display: inline-block;float: right;margin: 6px 15px 0;">上传站点信息
            </button>
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title" id="myModalLabel">上传站点信息</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row chart-row">
                            <div class="col-md-4">
                                <div class="form-inline">
                                    <div class="input-group">
                                        <label for="bianhao" class="control-label">站点编号：</label>
                                        <input type="text" name="bianhao" id="bianhao" placeholder="请输入站点编号"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-inline">
                                    <div class="input-group">
                                        <label for="zhandianmingcheng" class="control-label">站点名称：</label>
                                        <input type="text" name="zhandianmingcheng" id="zhandianmingcheng" placeholder="请输入站点名称"/>
                                    </div>
                                </div>
                            </div>
                                <div class="col-md-4">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <label for="fuzeren" class="control-label">负责人：</label>
                                            <input type="text" name="fuzeren" id="fuzeren" placeholder="请输入站点负责人"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="row chart-row">
                                <div class="col-md-4">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <label for="lianxidianhua" class="control-label">联系电话：</label>
                                            <input type="text" name="lianxidianhua" id="lianxidianhua" placeholder="11位移动电话"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <label for="youxiang" class="control-label">站点邮箱：</label>
                                            <input type="text" name="youxiang" id="youxiang" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="row chart-row">
                                <div class="col-md-4">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <label for="fuwushijian" class="control-label">服务时间：</label>
                                            <input type="text" name="fuwushijian" id="fuwushijian" placeholder="如：9点-22点"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <label for="dizhi" class="control-label">站点地址：</label>
                                            <input type="text" name="dizhi" id="dizhi" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                                <label for="jianjie">简介：</label>
                                <textarea rows="10" class="form-control" cols="30" maxlength="500" name="jianjie"
                                          id="jianjie"></textarea>
                            </div>
                            <script>
                                $(function () {


                                    //验证字符串是否是数字
                                    function checkNumber(theObj) {
                                        var reg = /^[0-9]+.?[0-9]*$/;
                                        if (reg.test(theObj)) {
                                            return true;
                                        }
                                        return false;
                                    }

                                    $("#btnupload").click(function () {
                                        var userid = "";
                                        if (document.getElementById("userid")) {
                                            userid = document.getElementById('userid').innerText;
                                            userid = userid.substring(5);
                                            userid = userid.replace(/\s*/g, "");
                                        }
                                        var bianhao = $("#bianhao").val();
                                        var zhandianmingcheng = $("#zhandianmingcheng").val();
                                        var fuzeren = $("#fuzeren").val();
                                        var lianxidianhua = $("#lianxidianhua").val();
                                        var youxiang = $("#youxiang").val();
                                        var fuwushijian = $("#fuwushijian").val();
                                        var dizhi = $("#dizhi").val();
                                        var jianjie = $("#jianjie").val();
                                        if (userid.length == 0) {
                                            alert("用户信息已失效！请重新登录");
                                            return false;
                                        }
                                        if (bianhao.length == 0) {
                                            alert("请填写站点编号！");
                                            return false;
                                        }
                                        if (fuzeren.length == 0) {
                                            alert("请填写站点负责人！");
                                            return false;
                                        }
                                        if (!checkNumber(lianxidianhua) || lianxidianhua.length != 11) {
                                            alert("请填写正确的电话号码！");
                                            return false;
                                        }
                                        if (youxiang.length == 0) {
                                            alert("请填写邮箱地址！");
                                            return false;
                                        }
                                        if (fuwushijian.length == 0) {
                                            alert("请填写服务时间！");
                                            return false;
                                        }
                                        if (dizhi.length == 0) {
                                            alert("请填写站点地址！");
                                            return false;
                                        }
                                        if (jianjie.length == 0) {
                                            alert("请填写站点简介！");
                                            return false;
                                        }
                                        var param = {
                                            "bianhao": bianhao,
                                            "zhandianmingcheng": zhandianmingcheng,
                                            "fuzeren": fuzeren,
                                            "lianxidianhua": lianxidianhua,
                                            "youxiang": youxiang,
                                            "fuwushijian": fuwushijian,
                                            "dizhi": dizhi,
                                            "jianjie": jianjie
                                        };
                                        // console.log(username);
                                        // console.log(userid);
                                        // console.log(wupinmingcheng.length);

                                        $.ajax({
                                            url: "/zhandian/information",
                                            type: "post",
                                            data: JSON.stringify(param),
                                            dataType: "json",
                                            contentType: "application/json;charset=UTF-8",
                                            //成功回调
                                            success: function (data) {
                                                if (data == "succeful") {
                                                    alert("上传成功！");
                                                } else if (data == "0"){
                                                    alert("该编号站点已存在！")
                                                } else {
                                                    alert("有空信息，上传失败！请重试！");
                                                }
                                                // data = $.parseJSON(data);
                                            },
                                            //超时时间
                                            timeout: 10000,
                                            //失败的回调
                                            error: function () {
                                                console.log("出错啦！")
                                            }
                                        });

                                    });
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
        </div>
        <div class="pre-scrollable">
            <div class="table-responsive" style="background-color: #ffffff">
                <table class="table table-striped ">
                    <thead>
                    <tr>
                        <th>站点编号</th>
                        <th>站点名称</th>
                        <th>负责人</th>
                        <th>联系电话</th>
                        <th>邮箱</th>
                        <th>服务时间</th>
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
