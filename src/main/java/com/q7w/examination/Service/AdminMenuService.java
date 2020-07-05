package com.q7w.examination.Service;

import com.q7w.examination.dao.AdminMenuDAO;
import com.q7w.examination.entity.AdminMenu;
import com.q7w.examination.entity.AdminRoleMenu;
import com.q7w.examination.entity.AdminUserRole;
import com.q7w.examination.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;
 //   @Resource
   // private RedisService redisService;

    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDAO.findAllByParentId(parentId);
    }

    public List<AdminMenu> getMenusByCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);
        List<AdminUserRole> userRoleList = adminUserRoleService.listAllByUid(user.getUId());
        List<AdminMenu> menus = new ArrayList<>();

        userRoleList.forEach(ur -> {
            List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(ur.getRid());
            rms.forEach(rm -> {
                AdminMenu menu = adminMenuDAO.findById(rm.getMid());
                // 防止多角色状态下菜单重复
                if(!menus.contains(menu)) {
                    menus.add(menu);
                }
            });
        });
        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<AdminMenu> menus = new ArrayList<>();
        List<AdminRoleMenu> rms = adminRoleMenuService.findAllByRid(rid);

        rms.forEach(rm -> menus.add(adminMenuDAO.findById(rm.getMid())));

        handleMenus(menus);
        return menus;
    }

    public void handleMenus(List<AdminMenu> menus) {
        menus.forEach(m -> {
            List<AdminMenu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });

        menus.removeIf(m -> m.getParentId() != 0);
    }
}
