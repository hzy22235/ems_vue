package com.baizhi.controller;


import com.baizhi.entity.Emp;
import com.baizhi.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("emp")
@CrossOrigin
@Slf4j
public class EmpController {
    @Autowired
    private EmpService empService;

    @Value("${photo.dir}")
    private String realPath;

    @GetMapping("findById")
    public Emp findById(String id){
        log.info("查询员工信息的id：[{}]",id);
        return empService.findById(id);
    }

    @GetMapping("findAll")
    public List<Emp> findAll(){
        return empService.findAll();
    }

    @GetMapping("delete")
    public Map<String,Object> delete(String id){
        log.info("删除员工的id:"+id);
        Map<String,Object> map=new HashMap<>();
        try{
            //先删除员工头像
            Emp emp=empService.findById(id);
            File file=new File(realPath,emp.getPath());
            if(file.exists()) file.delete();
            //再删除员工
            empService.delete(id);
            map.put("state",true);
            map.put("message","操作成功");
        }catch(Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("message","操作失败");
        }
        return map;
    }

    @PostMapping("save")
    public Map<String,Object> save(Emp emp, MultipartFile photo){
        log.info("员工信息[{}]",emp.toString());
        log.info("头像信息[{}]",photo.getOriginalFilename());
        Map<String,Object> map=new HashMap<>();
        try {
            //保存头像图片
            String newFileName=UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(photo.getOriginalFilename());//为了防止重名加了个uuid
            photo.transferTo(new File(realPath,newFileName));//将图像传到新的文件下下边
            //设置头像地址
            emp.setPath(newFileName);
            //保存员工信息
            empService.save(emp);
            map.put("state",true);
            map.put("message","员工保存成功");
        }catch (Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("message","员工保存失败");
        }
        return map;
    }

    @PostMapping("update")
    public Map<String,Object> update(Emp emp, MultipartFile photo){
        log.info("员工信息[{}]",emp.toString());

        Map<String,Object> map=new HashMap<>();
        try {
            if(photo!=null&&photo.getSize()!=0){
                log.info("头像信息[{}]",photo.getOriginalFilename());
                //保存头像图片
                String newFileName=UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(photo.getOriginalFilename());//为了防止重名加了个uuid
                photo.transferTo(new File(realPath,newFileName));//将图像传到新的文件下下边
                //设置头像地址
                emp.setPath(newFileName);
            }
            //保存员工信息
            empService.update(emp);
            map.put("state",true);
            map.put("message","员工保存成功");
        }catch (Exception e){
            e.printStackTrace();
            map.put("state",false);
            map.put("message","员工保存失败");
        }
        return map;
    }

}
