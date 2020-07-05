package com.q7w.examination.controller;

import com.q7w.examination.Service.AdminMenuService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.AdminMenu;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@RestController
public class MenuController {
    @Autowired
    AdminMenuService adminMenuService;
    @Autowired
    UserService userService;
    @Resource
    private RedisService redisService;

    @GetMapping("/api/menu_1")
    public List<AdminMenu> menu_1() {
        return adminMenuService.getMenusByCurrentUser();
    }
    @RequestMapping(value = "/api/menu/refreshmenu", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseData refreshmecu() throws Exception {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        redisService.del(username+"menu");
        return new ResponseData(ExceptionMsg.SUCCESS,"缓存清理成功");
    }
    @GetMapping("/api/menu")
    public ResponseData menu() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Object usermenu = redisService.get(username+"menu");
        if(usermenu == null){
            List<Object> admin_user_menu = new ArrayList<Object>(adminMenuService.getMenusByCurrentUser());
            if(admin_user_menu.size()!=0){
                redisService.set(username+"menu",admin_user_menu,7200);
                usermenu = admin_user_menu;
                return new ResponseData(ExceptionMsg.SUCCESS_GETUSER,usermenu);
            }else {
                return new ResponseData(ExceptionMsg.FAILED_V,"用户不存在,请重新尝试");
            }
        }else {
            return new ResponseData(ExceptionMsg.SUCCESS_GETUSERR,usermenu);
        }
    }
    @GetMapping("/api/admin/role/menu")
    public ResponseData listAllMenus() {
        return new ResponseData(ExceptionMsg.SUCCESS_GETUSER,adminMenuService.getMenusByRoleId(1));
    }

}
