package com.example.xcschoolserver.mapper;


import com.example.xcschoolserver.pojo.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface adminUserMapper {
    // 查询用户名密码
    AdminUser selectAdminUser(String adminUserName, String adminPassword);

    //根据userName将token更新到数据库
    int updataToken(String adminUserName, String token);

    //将新密码更新到数据库
    int updataPwd(String adminUserName, String adminPassword);

    //通过token查询登录状态
    AdminUser selectToken(String token);
}

