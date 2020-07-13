package com.q7w.examination.controller;

import com.q7w.examination.Service.AdminRoleService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.User;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.Pbkdf2Sha256;
import com.q7w.examination.util.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "用户管理相关接口")
public class UserController implements Serializable {
    private static final long serialVersionUID = 3033545151355633240L;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    UserService userService;

    @GetMapping("/api/admin/role_1")
    public ResponseData listRoles_1(){
        return new ResponseData(ExceptionMsg.SUCCESS,adminRoleService.list());
    }
    @GetMapping("/api")
    @CrossOrigin
    public List<User> list()  {

        List<User> userList = userDAO.findAll();
        return userList;
        //return new ResponseData(ExceptionMsg.SUCCESS,userList);
    }
    @PutMapping("/api/admin/user/status")
    public  ResponseData updateUserStatus(@RequestBody User requestUser) {
        if (userService.updateUserStatus(requestUser)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"SUCCESS");
        } else {
            return new ResponseData(ExceptionMsg.FAILED,"faile");
        }
    }
    @GetMapping("/api/admin/user")
    @CrossOrigin
    @RequiresRoles("admin")
    public ResponseData listUsers_1() {
        try {
            return new ResponseData(ExceptionMsg.SUCCESS,userService.list());
        } catch (Exception e) {
            return new ResponseData(ExceptionMsg.FAILED,"faile");
        }
    }

    @RequestMapping(value="/api_1",method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData getlist() {
        List<User> userList = new ArrayList<>(userDAO.findAll());
        return new ResponseData(ExceptionMsg.SUCCESS,userList);
    }
    @ApiOperation("添加用户的接口")

    @PutMapping("/api/admin/user")
    public ResponseData editUser(@RequestBody User requestUser) {
        if(userService.editUser(requestUser)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"SUCCESS");
        } else {
            return new ResponseData(ExceptionMsg.FAILED,"faile");
        }
    }
    @RequestMapping("/user/list")
    public ModelAndView articlelist(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                    @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        start = start < 0 ? 0 : start;
        Sort sort = Sort.by(Sort.Direction.DESC, "UId");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<User> page = userDAO.findAll(pageable);
        ModelAndView mav = new ModelAndView("User/list");
        mav.addObject("page", page);
        return mav;
    }

    //前端用户注册
    //http://localhost:8080/saveUser?userName=xiaoli8&userPassword=123
    @GetMapping("/addUser" )
     public String addUser(){
            return "/User/add";
            }



    //http://localhost:8080/updateUser?Id=1&userName=%E5%A4%A7%E8%80%81%E6%9D%A8&userPassword=1111
    @GetMapping(value = "/updateUser")
    public String updateUser(int UId,String userName,String userPassword){
        User user = new User(UId,userName, userPassword);
        userDAO.save(user);
        return "success";
    }

    @PostMapping("/api/userdelete")
    public ResponseData deleteUser(@RequestBody User user) {
        if(userService.deleteUser(user)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"删除成功");
        } else {
            return new ResponseData(ExceptionMsg.FAILED,"更新异常");
        }
    }
    @GetMapping("/api/initvcode")
    public ResponseData initvcodebyphone(@RequestParam(value = "phone") String phone){
        if(userService.wxisExist(phone)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"生成成功，有效期为5min");
        }else {
            return new ResponseData(ExceptionMsg.FAILED,"生成失败请重试");
        }
    }


}
