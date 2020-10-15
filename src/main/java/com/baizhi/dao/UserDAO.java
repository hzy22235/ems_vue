package com.baizhi.dao;

import com.baizhi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper//一定要加这个注解  不然Service层找不到这个东西
public interface UserDAO {
    void save(User user);
    User findByUserName(String username);

}
