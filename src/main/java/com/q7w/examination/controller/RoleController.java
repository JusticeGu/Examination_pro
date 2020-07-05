package com.q7w.examination.controller;

import com.q7w.examination.Service.AdminPermissionService;
import com.q7w.examination.Service.AdminRoleMenuService;
import com.q7w.examination.Service.AdminRolePermissionService;
import com.q7w.examination.Service.AdminRoleService;
import com.q7w.examination.entity.AdminRole;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


@RestController
public class RoleController {
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    @GetMapping("/api/admin/role")
    public ResponseData listRoles(){
        return new ResponseData(ExceptionMsg.SUCCESS,adminRoleService.list());
    }

    @PutMapping("/api/admin/role/status")
    public ResponseData updateRoleStatus(@RequestBody AdminRole requestRole) {
        AdminRole adminRole = adminRoleService.updateRoleStatus(requestRole);
        String message = "用户" + adminRole.getNameZh() + "状态更新成功";
        return new ResponseData(ExceptionMsg.SUCCESS,message);
    }

    @PutMapping("/api/admin/role")
    public ResponseData editRole(@RequestBody AdminRole requestRole) {
        adminRoleService.addOrUpdate(requestRole);
        adminRolePermissionService.savePermChanges(requestRole.getId(), requestRole.getPerms());
        String message = "修改角色信息成功";
        return new ResponseData(ExceptionMsg.SUCCESS,message);
    }


    @PostMapping("/api/admin/role")
    public ResponseData addRole(@RequestBody AdminRole requestRole) {
        if (adminRoleService.editRole(requestRole)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"SUCCESS1");
        } else {
            return new ResponseData(ExceptionMsg.FAILED,"修改失败");
        }
    }

    @GetMapping("/api/admin/role/perm")
    public ResponseData listPerms() {
        return new ResponseData(ExceptionMsg.SUCCESS,adminPermissionService.list());
    }

    @PutMapping("/api/admin/role/menu")
    public ResponseData updateRoleMenu(@RequestParam int rid, @RequestBody LinkedHashMap menusIds) {
        if(adminRoleMenuService.updateRoleMenu(rid, menusIds)) {
            return new ResponseData(ExceptionMsg.SUCCESS,"SUCCESS1");
        } else {
            return new ResponseData(ExceptionMsg.FAILED_V,"修改失败");
        }
    }
}
