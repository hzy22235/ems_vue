package com.baizhi.service;

import com.baizhi.dao.UserDAO;
import com.baizhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDAO userDAO;

    @Override
    public User login(User user) {
        //根据用户输入用户名进行查询
        User userDB=userDAO.findByUserName(user.getUsername());
        if(userDB!=null){
            //比较密码
            if(userDB.getPassword().equals(user.getPassword())){
                return userDB;
            }else{
                throw new RuntimeException("密码不正确");
            }
        }else{
            throw new RuntimeException("用户名不存在");
        }
    }

    @Override
    public void register(User user) {
        User tmp=userDAO.findByUserName(user.getUsername());
        if(tmp==null){
            user.setStatus("已激活");
            user.setRegisterTime( new Date());
            userDAO.save(user);
        }else{
            throw new RuntimeException("用户名已存在");
        }
    }
}
