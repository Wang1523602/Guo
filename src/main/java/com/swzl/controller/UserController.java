package com.swzl.controller;

import com.swzl.entity.Notice;
import com.swzl.entity.User;
import com.swzl.service.NoticeService;
import com.swzl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lscl
 * @version 1.0
 * @intro:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户登录
     *
     * @param user
     * @param checkCode
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/login")
    public String login(User user, String checkCode, HttpSession session,Model model) {
        Object sessionCode = session.getAttribute("sessionCode");
        if (!checkCode.equals(sessionCode)) {
            model.addAttribute("errorMsg", "验证码错误");
            return "/login.jsp";
        }
        String authority = user.getAuthority();
        if ("用户".equals(authority)) {
            User dbUser = userService.findByUsername(user.getUsername());
            if (null != dbUser && user.getPassword().equals(dbUser.getPassword())) {
                session.setAttribute("loginUser", dbUser);
//            return "/index.jsp";
                return "redirect:/index.jsp";
            } else {
                model.addAttribute("errorMsg", "用户名或密码错误");
                return "/login.jsp";
            }
        } else {
            User dbUser = userService.findByUsername(user.getUsername());
            if (null != dbUser && user.getPassword().equals(dbUser.getPassword()) && dbUser.getAuthority().contains("管理员")) {
                session.setAttribute("loginUser", dbUser);
//            return "/index.jsp";
                return "redirect:/houTaiIndex.jsp";
            } else {
                model.addAttribute("errorMsg", "用户名或密码错误");
                return "/login.jsp";
            }
        }
    }


    //ajax验证用户名称是否可用username
    @RequestMapping("/checkName")
    @ResponseBody//返回json数据到前端
    public boolean ajaxCheckName(String username) {
        username = username.trim();
        if (StringUtils.isEmpty(username) || username.length() == 0 || username.length() > 10) {
            return false;
        }
        User resCheckNameUser = userService.findByUsername(username);
        if (null != resCheckNameUser) {
            return false;
        } else {
            return true;
        }
    }


    //ajax返回注册信息
    @RequestMapping("/register")
    @ResponseBody//返回字符串数据到前端
    public String ajaxCheckRegister(@RequestBody User user) {
        String username = user.getUsername().trim();
        String password = user.getPassword().trim();
        if (username.equals("")) {
            return "注册失败，用户名为空！";
        }
        if (password.equals("")) {
            return "注册失败，密码为空！";
        }
        User resCheckRegister = userService.findByUsername(user.getUsername());
        if (resCheckRegister != null && resCheckRegister.getUsername().equals(user.getUsername())) {
            return "注册失败，该用户已存在！";
        } else {
            Date date = new Date();
            if (user.getSex().equals("M")) {
                user.setSex("男");
            } else {
                user.setSex("女");
            }
            user.setAuthority("普通用户");
            user.setAddtime(date);
            userService.add(user);
//            Integer id = user.getId();
            return "注册成功！";
        }
    }

    //注销登录
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        String res = "{\"status\":\"200\",\"result\":\"session清空！\"}";
        return res;
    }

    //后台超级管理员获取普通用户列表信息
    @RequestMapping("/HTUserList")
    @ResponseBody//返回字符串数据到前端
    public String HTUserList(@RequestBody User user) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        Integer userid = user.getId();
        User dbUser = userService.findByUserId(userid);
        if (null == dbUser || !"超级管理员".equals(dbUser.getAuthority())) {
            ggJson.append("\"status\":" + "\"0\"");
        } else {
            List<User> userEntitys = userService.findAllUser();
            if (userEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < userEntitys.size(); i++) {
                    User userEntity = userEntitys.get(i);
                    Integer id = userEntity.getId();
                    String username = userEntity.getUsername();
                    String sex = userEntity.getSex();
                    String authority = userEntity.getAuthority();
                    Date addtime = userEntity.getAddtime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"username\":" + "\"" + username + "\"" + ",");
                    ggJson.append("\"sex\":" + "\"" + sex + "\"" + ",");
                    ggJson.append("\"authority\":" + "\"" + authority + "\"" + ",");
                    ggJson.append("\"date\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < userEntitys.size() - 1) {
                        ggJson.append(",");
                    }
                }
                ggJson.append("]");
            } else {
                ggJson.append("\"status\":" + "\" false \"");
            }
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //后台超级管理员获取普通管理员用户列表信息
    @RequestMapping("/HTUserGLList")
    @ResponseBody//返回字符串数据到前端
    public String HTUserGLList(@RequestBody User user) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        Integer userid = user.getId();
        User dbUser = userService.findByUserId(userid);
        if (null == dbUser || !"超级管理员".equals(dbUser.getAuthority())) {
            ggJson.append("\"status\":" + "\"0\"");
        } else {
            List<User> userEntitys = userService.findAllGLUser();
            if (userEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < userEntitys.size(); i++) {
                    User userEntity = userEntitys.get(i);
                    Integer id = userEntity.getId();
                    String username = userEntity.getUsername();
                    String sex = userEntity.getSex();
                    String authority = userEntity.getAuthority();
                    Date addtime = userEntity.getAddtime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"username\":" + "\"" + username + "\"" + ",");
                    ggJson.append("\"sex\":" + "\"" + sex + "\"" + ",");
                    ggJson.append("\"authority\":" + "\"" + authority + "\"" + ",");
                    ggJson.append("\"date\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < userEntitys.size() - 1) {
                        ggJson.append(",");
                    }
                }
                ggJson.append("]");
            } else {
                ggJson.append("\"status\":" + "\" false \"");
            }
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //超级管理员修改用户权限为管理员
    @GetMapping("/HTGLUser")
    public String HTGLUser(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        userService.updateGL(id);
        return "redirect:/houTaiUser.jsp";
    }

    //超级管理员修改用户权限为普通用户
    @GetMapping("/HTQXUser")
    public String HTQXUser(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        userService.updateQX(id);
        return "redirect:/houTaiUser.jsp";
    }

    //超级管理员删除用户
    @GetMapping("/HTUserDelete")
    public String HTUserDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        userService.delete(id);
        return "redirect:/houTaiUser.jsp";
    }


}
