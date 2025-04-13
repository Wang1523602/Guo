package com.swzl.service;

import com.swzl.entity.User;
import com.swzl.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lscl
 * @version 1.0
 * @intro:
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findByUserId(Integer userid) {
        return userMapper.findByUserId(userid);
    }

    /**
     * 用户新增
     *
     * @param user
     */
    public void add(User user) {
        userMapper.add(user);
    }

    public List<User> findAllUser() {
        return userMapper.findAllUser();
    }

    //全部管理员
    public List<User> findAllGLUser() {
        return userMapper.findAllGLUser();
    }

    public void updateGL(String id) {
        userMapper.updateGL(id);
    }

    //修改权限为普通用户，即取消管理员权限
    public void updateQX(String id) {
        userMapper.updateQX(id);
    }

    //超级管理员删除用户
    public void delete(String id) {
        userMapper.delete(id);
    }
}
