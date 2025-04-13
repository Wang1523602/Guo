<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>用户登录</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script type="text/javascript">
    </script>
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
</head>
<body>
<div class="container" style="width: 400px;">
    <h1 style="text-align: center;">SSM社区线上失物招领系统</h1>
    <h3 style="text-align: center;">用户登录界面</h3>
    <form action="/user/login" method="post">
        <div class="form-group">
            <label for="user">用户名：</label>
            <input type="text" name="username" class="form-control" id="user" placeholder="请输入用户名"/>
        </div>

        <div class="form-group">
            <label for="password">密码：</label>
            <input type="password" name="password" class="form-control" id="password" placeholder="请输入密码"/>
        </div>
        <div class="form-inline">
            <label for="code">验证码：</label>
            <input type="text" name="checkCode"
                   class="form-control" id="verifycode"
                   placeholder="请输入验证码" style="width: 120px;"/>
            <a onclick="document.getElementById('code').src='/checkCode.jsp?v='+Math.random()">
                <img src="/checkCode.jsp" title="看不清点击刷新" id="code"/>
            </a>
        </div>
        <br>
        <div class="form-group">
            <label class="radio-inline">
                <input type="radio" name="authority" id="authority1" value="用户" checked> 用户登录
            </label>
            <label class="radio-inline">
                <input type="radio" name="authority" id="authority2" value="管理员"> 管理员登录
            </label>
        </div>
        <hr/>
        <h4>${errorMsg}</h4>
        <div class="form-group" style="text-align: center;">
            <input class="btn btn-primary btn-lg btn-block" type="submit" value="登录">
            <input class="btn btn-warning btn-lg btn-block" type="reset" value="重置">
            <a type="test" class="btn btn-link" href="/register.jsp"><p class="lead"><strong>没有账号？立即注册</strong></p></a>
        </div>

    </form>

</div>
</body>
</html>