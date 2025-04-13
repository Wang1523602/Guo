<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>用户注册</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <style type="text/css">
        body {
            width: 100%;
            height: 100%;
            background: #c7c7c7;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            background-attachment: fixed;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $("#username").blur(function () {
                var username =$("#username").val();
                username =username.replace(/\s*/g, "");
                console.log(username);
                if (username.length==0){
                    $("#msg").text("用户名为空！");
                    return false;
                }
                if (username.length>10){
                    $("#msg").text("用户名过长！");
                    return false;
                }
                $.ajax({
                    url:"/user/checkName",
                    data:{"username":username},
                    type:"post",
                    dataType:"json",
                    //成功回调
                    success:function (data) {
                        // console.log(typeof (data));
                        if (data){
                            $("#msg").text("该用户名可以使用！");
                        }else {
                            $("#msg").text("用户名被占用或为空，请换一个用户名！");
                        }
                    },
                    //超时时间
                    timeout:2000,
                    //失败的回调
                    error:function () {
                        console.log("出错啦！")
                    }
                });
            });

            $("#btn").click(function () {
                var username =$("#username").val();
                var password =$("#password").val();
                var sex =$(":radio:checked").val();
                $.ajax({
                    url:"/user/register",
                    type:"post",
                    dataType:"text",
                    data:JSON.stringify({"username":username,"password":password,"sex":sex}),
                    contentType:"application/json;charset=UTF-8",
                    success:function (data) {
                        alert(data+"");
                    },
                    //超时时间
                    timeout:2000,
                    //失败的回调
                    error:function () {
                        console.log("出错啦！")
                    }
                });
            });
        });
    </script>
</head>

<body>
<div class="container" style="width: 400px;">
    <h1 style="text-align: center;">SSM社区线上失物招领系统</h1>
    <h3 style="text-align: center;">用户注册界面</h3>
    <form>
        <div class="form-group">
            <label for="username">用户名：</label>
            <input type="text" name="username" class="form-control" id="username" placeholder="请输入用户名（请不要使用空格）"/><span id="msg"></span>
        </div>
        <div class="form-group">
            <label for="password">密码：</label>
            <input type="password" name="password" class="form-control" id="password" placeholder="请输入密码"/>
        </div>
        <div class="form-group">
            <label>性别：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
            <input type="radio" name="userGender" id="genderM" value="M" checked>男
            <input type="radio" name="userGender" id="genderF" value="F">女
        </div>
        <span id="btnmsg"></span>
        <div class="form-group" style="text-align: center;">
            <input class="btn btn-primary btn-lg btn-block" type="button" id="btn" value="注册">
            <a type="test" class="btn btn-link" href="/login.jsp"><p class="lead"><strong>已有账号？登录</strong></p></a>
        </div>
    </form>
    <p class="text-center">温馨提示，用户名不可重复！</p>
    <p class="text-center"> 如果提示用户名已被他人使用，请重新填写用户名！</p>
</div>
</body>
</html>