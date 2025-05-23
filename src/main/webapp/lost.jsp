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

    <title>招领界面</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/indexcss/carousel.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        // var pageNumber = $("li[class=active]").val();
        // console.log(pageNumber);
        // console.log(typeof pageNumber);
        var totalPage = 1;
        $(document).ready(function () {
            var currPage = sessionStorage.getItem('lostCurrPage');
            // console.log(currPage);
            if (currPage == null) {
                currPage = 1;
            }
            LoadList(currPage);
            $("#prevPageId").click(function () {
                if (currPage > 1) {
                    currPage--;
                    LoadList(currPage);
                }
            });
            $("#nextPageId").click(function () {
                if (currPage < totalPage) {
                    currPage++;
                    LoadList(currPage);
                }

            });
            $("#firstPageId").click(function () {
                currPage = 1;
                LoadList(currPage);
            });
            $("#lastPageId").click(function () {
                currPage = totalPage;
                LoadList(currPage);
            });
            $("#logout").click(function () {
                logOut();
            });
        });

        function LoadList(currPage) {
            var param = {"currPage": currPage};
            //异步请求
            $.ajax({
                url: "/lost/list.do",
                type: "post",
                data: JSON.stringify(param),
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
                        let list = '';
                        //重置标题列表
                        var liArrays = $("ul[class=list-group]").children();
                        // console.log(liArrays.length)
                        for (i = 0; i < liArrays.length; i++) {
                            liArrays[i].remove();
                        }
                        //总页数
                        totalPage = parseInt(data.totalPage);
                        //总记录数
                        var totalCount = parseInt(data.totalCount);
                        //当前页
                        var currPage = parseInt(data.currPage);
                        sessionStorage.setItem('lostCurrPage', currPage);
                        data = data.list;
                        $.each(data, function (index, listEntity) {
                            list += '   <li class="list-group-item">\n' +
                                '                    <span class="badge">' + listEntity.addtime + '</span>\n' +
                                '                    <a style="color:#5A5A5A;" href="/lost/content?id=' + listEntity.id + '">' + listEntity.wupinmingcheng + '（' + listEntity.zhuangtai + '）\n</a>' +
                                '                </li>';
                        });
                        $('ul[class=list-group]').append(list);
                        $('#currPage').text("当前的页码：" + currPage);
                        $('#totalPage').text("总页数：" + totalPage);
                        $('#totalCount').text("总记录数：" + totalCount);
                    } else {
                        // console.log("false");
                        alert(data + "参数错误!");
                    }

                },
                //超时时间
                timeout: 10000,
                //失败的回调
                error: function () {
                    console.log("列表标题出错啦！")
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

<div class="container" style="margin-top: 100px">
    <div style="background-color: #f5f5f5;border-radius: 5px;">
        <ul class="breadcrumb" style="display: inline-block;margin-bottom: 5px">
            <li><a href="/index.jsp" style="color:#5A5A5A;">社区线上失物招领系统</a></li>
            <li class="active"><a style="color:#5A5A5A;">招领公告</a></li>
        </ul>
        <button type="button" data-toggle="modal" data-target="#myModal"
                style="display: inline-block;float: right;margin: 6px 15px 0;">上传物品信息
        </button>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" id="myModalLabel">上传招领信息</h4>
                    </div>
                    <div class="modal-body">
                        <div style="display: inline-block;float:left;width:300px;overflow:hidden;">
                            <label>相关图片：</label>
                            <input type="file" id="img_upload"/>
                            <%--<textarea id="base64_code" rows="30" cols="100"></textarea>--%>
                            <p id="img_area"></p>
                        </div>
                        <div style="display: inline-block;margin-left:0px;">
                            <div class="form-group">
                                <label for="wupinmingcheng">物品名称：</label>
                                <input type="text" name="wupinmingcheng" id="wupinmingcheng" placeholder="请输入物品名称"/>
                                <label for="lianxidianhua">&nbsp;&nbsp;&nbsp;联系电话：</label>
                                <input type="text" name="lianxidianhua" id="lianxidianhua"/>
                            </div>
                            <div class="form-group">
                                <label for="jiandaodizhi">捡到地址：</label>
                                <input type="text" name="jiandaodizhi" id="jiandaodizhi"/>
                                <label for="jiandaoriqi">&nbsp;&nbsp;&nbsp;捡到日期：</label>
                                <input type="date" name="jiandaoriqi" id="jiandaoriqi"/>
                            </div>
                            <div class="form-group">
                                <label for="zhandianmingcheng">归还站点：</label>
                                <input type="text" list="test" value='爱心A站点' onfocus="this.value=''"
                                       name="zhandianmingcheng" id="zhandianmingcheng"/>
                                <datalist id="test">
                                    <option value="爱心A站点"></option>
                                    <option value="爱心B站点"></option>
                                    <option value="爱心C站点"></option>
                                    <option value="爱心D站点"></option>
                                    <option value="爱心E站点"></option>
                                    <option value="爱心F站点"></option>
                                </datalist>
                                <label for="daozhandianriqi">&nbsp;&nbsp;&nbsp;到站点日期：</label>
                                <input type="date" name="daozhandianriqi" id="daozhandianriqi"/>
                            </div>
                            <div class="form-group">
                                <label for="jianshu">简述：</label>
                                <textarea rows="10" class="form-control" cols="30" maxlength="500" name="jianshu"
                                          id="jianshu"></textarea>
                            </div>
                        </div>
                        <script>
                            $(function () {
                                var base64Str = "";
                                window.onload = function () {

                                    // 抓取上传图片，转换代码结果，显示图片的dom

                                    var img_upload = document.getElementById("img_upload");

                                    // var base64_code = document.getElementById("base64_code");

                                    var img_area = document.getElementById("img_area");

                                    // 添加功能出发监听事件
                                    img_upload.addEventListener('change', readFile, false);
                                }

                                function readFile() {
                                    var file = this.files[0];//这里是抓取到上传的对象。
                                    // console.log(file.size);
                                    if (!/image\/\w+/.test(file.type) || (file.size) / 1024 > 2048) {
                                        alert("上传失败！请确保文件为图像类型且图片不超过2MB");
                                        return false;
                                    }
                                    var reader = new FileReader();
                                    reader.readAsDataURL(file);
                                    reader.onload = function () {
                                        // base64_code.innerHTML = this.result;
                                        // console.log(this.result);
                                        // console.log(typeof (this.result));
                                        base64Str = this.result;
                                        // console.log(base64Str);
                                        // console.log(base64Str.length);
                                        //this.result里的这个result是FileReader.readAsDataURL()接口当中转换完图片输出的base64结果存放在result当中
                                        img_area.innerHTML = '<div>图片预览：</div><img src="' + this.result + '"  height="220" width="255"  alt=""/>';
                                    }
                                }

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
//存在
                                        userid = document.getElementById('userid').innerText;
                                        userid = userid.substring(5);
                                        userid = userid.replace(/\s*/g, "");
                                    }
                                    var wupinmingcheng = $("#wupinmingcheng").val();
                                    var jiandaodizhi = $("#jiandaodizhi").val();
                                    var jiandaoriqi = $("#jiandaoriqi").val();
                                    var zhandianmingcheng = $("#zhandianmingcheng").val();
                                    var daozhandianriqi = $("#daozhandianriqi").val();
                                    var lianxidianhua = $("#lianxidianhua").val();
                                    var jianshu = $("#jianshu").val();
                                    if (userid.length == 0) {
                                        alert("用户信息已失效！请重新登录");
                                        return false;
                                    }
                                    if (wupinmingcheng.length == 0) {
                                        alert("请填写物品名称！");
                                        return false;
                                    }
                                    if (zhandianmingcheng.length == 0) {
                                        alert("请选择站点！");
                                        return false;
                                    }
                                    if (daozhandianriqi.length == 0) {
                                        alert("请选择到站点日期！");
                                        return false;
                                    }
                                    if (!checkNumber(lianxidianhua) || lianxidianhua.length != 11) {
                                        alert("请填写正确的电话号码！");
                                        return false;
                                    }
                                    if (jiandaodizhi.length == 0) {
                                        alert("请填写捡到地址！");
                                        return false;
                                    }
                                    if (jiandaoriqi.length == 0) {
                                        alert("请选择捡到日期！！");
                                        return false;
                                    }
                                    if (jianshu.length == 0) {
                                        alert("请填写相关简述！");
                                        return false;
                                    }
                                    if (base64Str.length == 0) {
                                        alert("请上传图片！");
                                        return false;
                                    }
                                    var param = {
                                        "userid": userid,
                                        "wupinmingcheng": wupinmingcheng,
                                        "jiandaodizhi": jiandaodizhi,
                                        "jiandaoriqi": jiandaoriqi,
                                        "zhandianmingcheng": zhandianmingcheng,
                                        "daozhandianriqi": daozhandianriqi,
                                        "lianxidianhua": lianxidianhua,
                                        "tupian": base64Str,
                                        "jianshu": jianshu,
                                        "zhuangtai": "未领取"
                                    };
                                    // console.log(username);
                                    // console.log(userid);
                                    // console.log(wupinmingcheng.length);

                                    $.ajax({
                                        url: "/upload/information",
                                        type: "post",
                                        data: JSON.stringify(param),
                                        dataType: "json",
                                        contentType: "application/json;charset=UTF-8",
                                        //成功回调
                                        success: function (data) {
                                            if (data == "succeful") {
                                                alert("上传成功，等待管理员审核！")
                                            } else {
                                                alert("上传失败！请重试！")
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
    <div class="row" style="padding:0 15px;">
        <ul class="list-group">

        </ul>
        <div style="text-align:center">
            <div class="pagination" style="color: #ffffff">
                <a id="firstPageId" style="color: #ffffff">首页</a>
                <a id="prevPageId" style="color: #ffffff">上一页</a>
                <a id="nextPageId" style="color: #ffffff">下一页</a>
                <a id="lastPageId" style="color: #ffffff">尾页</a>
                <span id="currPage"></span>
                <span id="totalPage"></span>
                <span id="totalCount"></span>
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
