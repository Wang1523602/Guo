package com.swzl.mapper;

import com.swzl.entity.User;

import java.util.List;

/**
 * @author lscl
 * @version 1.0
 * @intro:
 */
public interface UserMapper {
    User findByUsername(String username);

    void add(User user);

    User findByUserId(Integer userid);

    //全部普通用户
    List<User> findAllUser();

    //全部管理员
    List<User> findAllGLUser();

    //修改权限为管理员
    void updateGL(String id);

    //修改权限为普通用户，即取消管理员权限
    void updateQX(String id);

    //超级管理员删除用户
    void delete(String id);
}
