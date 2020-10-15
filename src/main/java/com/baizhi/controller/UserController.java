package com.baizhi.controller;


import com.baizhi.service.UserService;
import com.baizhi.utils.VerifyCodeUtils;
import com.baizhi.entity.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Map<String,Object> login(@RequestBody  User user){
        log.info("登录用户：[{}]",user.toString());
        Map<String,Object> map=new HashMap<>();
        try {
            User userDB=userService.login(user);
            map.put("state",true);
            map.put("message","登录成功");
            map.put("user",userDB);
        }catch (Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("message","提示："+e.getMessage());
        }
        return map;
    }
    /**
     * 验证码
     */
    @GetMapping("getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        String code=VerifyCodeUtils.generateVerifyCode(4);
        //放入servletContext作用域,后边的注册用户要用到这个
        request.getServletContext().setAttribute("code",code);
        //将图片转化为base64
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120,30,byteArrayOutputStream,code);
        String s=Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
        return "data:image/png;base64,"+s;
    }

    /*
        这里用Map来进行对象的返回
     */
    @PostMapping("register")
    public Map<String,Object> register(@RequestBody User user, String code, HttpServletRequest request){
        log.info("用户信息：[{}]",user.toString());
        log.info("用户输入的验证码信息：[{}]",code);
        Map<String,Object> map=new HashMap<>();
        //调用业务方法
        try {
            String key = (String) request.getServletContext().getAttribute("code");
            if(key.equalsIgnoreCase(code)){
                userService.register(user);
                map.put("state",true);
                map.put("message","注册成功");

            }else{
                throw new RuntimeException("验证码错误");
            }

        }catch(Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("message","提示："+e.getMessage());
        }
        return map;

    }
}
