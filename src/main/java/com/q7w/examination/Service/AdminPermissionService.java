package com.q7w.examination.Service;


import com.q7w.examination.dao.AdminPermissionDAO;
import com.q7w.examination.entity.Uesr.AdminPermission;
import com.q7w.examination.entity.Uesr.AdminRole;
import com.q7w.examination.entity.Uesr.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminPermissionService {
    @Autowired
    AdminPermissionDAO adminPermissionDAO;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    UserService userService;

    public AdminPermission findById(int id) {
        return adminPermissionDAO.findById(id);
    }

    public List<AdminPermission> list() {return adminPermissionDAO.findAll();}

    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = adminPermissionDAO.findAll();
        for (AdminPermission p: ps) {
            // 这里我们进行前缀匹配，拥有父权限就拥有所有子权限
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<AdminRolePermission> rps = adminRolePermissionService.findAllByRid(rid);
        List<AdminPermission> perms = new ArrayList<>();
        rps.forEach(rp -> perms.add(adminPermissionDAO.findById(rp.getPid())));
        return perms;
    }

    public Set<String> listPermissionURLsByUser(String username) {
        List<AdminRole> roles = adminRoleService.listRolesByUser(username);
        Set<String> URLs = new HashSet<>();

        roles.forEach(r -> {
            List<AdminRolePermission> rps = adminRolePermissionService.findAllByRid(r.getId());
            rps.forEach(rp -> URLs.add(adminPermissionDAO.findById(rp.getPid()).getUrl()));
        });

        return URLs;
    }


}
