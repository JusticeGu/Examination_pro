package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.AdminRoleService;
import com.q7w.examination.Service.AdminUserRoleService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.Uesr.AdminRole;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.Pbkdf2Sha256;
import com.q7w.examination.util.RandomUtil;
import com.q7w.examination.util.ValidUtil;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Resource
    private RedisService redisService;


    private static final String WXAPPID = "wxc4d82adbcb40d914";
    private static final String WXSECRET = "a789fb8efbbc3aea7729d9c759de2568";
    @Override
    public List<UserDTO> list() {
        List<User> users =  userDAO.findAll();
        List<AdminRole> roles;

        List<UserDTO> userDTOS = users.stream().map(user -> (UserDTO) new UserDTO().convertFrom(user)).collect(Collectors.toList());

        for (UserDTO userDTO : userDTOS) {
            roles = adminRoleService.listRolesByUser(userDTO.getUsername());
            userDTO.setRoles(roles);
        }
        return userDTOS;
    }
    @Override
    /**
     * 重置密码/重置为手机号后6位
     */
    public boolean resetPassword(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //  int times = 2;
        //String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        userInDB.setSalt(salt);
        String encodedPassword = Pbkdf2Sha256.encode(userInDB.getPhone().substring(6,11),salt);
        userInDB.setUser_password(encodedPassword);
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean changePassword(String oldpwd, String pwd) {
        String username = getusernamebysu();
        return false;
    }

    @Override
    public boolean isExist(String username) {
        User user = userDAO.findByUsername(username);
        return null!=user;
    }

    /**
     * 用户名邮箱是否匹配
     * @param username
     * @param email
     * @return
     */
    @Override
    public boolean usernamemailcheck(String username, String email) {
        if (username.isEmpty()||email.isEmpty()){return false;}
        if (redisService.hasKey("PWRS:"+username)){return false;}
        User user = userDAO.findByUsername(username);
        if (user.getEmail().equals(email)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String usernametouno(String username){
        User user = userDAO.findByUsername(username);
        return user.getUno();
    }
    @Override
    public boolean isable(String username) {
        User user = userDAO.findByUsername(username);
        boolean status = user.isEnabled();
        return status;
    }
    @Override
    public boolean wxisable(String wxuid) {
        User user = userDAO.findByWxuid(wxuid);
        boolean status = user.isEnabled();
        return status;
    }
    @Override
    public boolean wxisExist(String wxuid) {
        User user = userDAO.findByWxuid(wxuid);
        return null!=user;
    }
    @Override
    public boolean deleteUser(User user) {
        User userInDB = userDAO.findById(user.getUId());
        //   redisService.del(userInDB.getWxuid(),userInDB.getUsername()+"menu");
        try {
            userDAO.deleteByUId(user.getUId());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    @Override
    public User findByWx_uid(String wxuid) {
        return userDAO.findByWxuid(wxuid);
    }
    @Override
    public void addOrUpdate(User user) {
        userDAO.save(user);
    }
    @Override
    public boolean updateUserStatus(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        int status = user.getActive_status();
        userInDB.setEnabled(user.isEnabled());
        try {
            userDAO.save(userInDB);
            if(redisService.hasKey(userInDB.getWxuid())){
                redisService.del(userInDB.getWxuid());}
        } catch (IllegalArgumentException e) {
            return false;
        } return true;
    }
    @Override
    public boolean editUser(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userInDB.setName(user.getName());
        userInDB.setWxuid(user.getWxuid());
        try {
            userDAO.save(userInDB);
            adminUserRoleService.saveRoleChanges(userInDB.getUId(), user.getRoles());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    @Override
    public int register(User user) {
        String username = user.getUsername();;
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getUser_password();
        String create_by = user.getCreateBy();
        String name = user.getName();
        String uno = user.getUno();
        Date now= new Date();
        Long createtime = now.getTime();
        Long updatetime = now.getTime();
        user.setCreateTime(createtime);
        user.setUpdateTime(updatetime);
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(username);
        user.setName(name);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        uno = HtmlUtils.htmlEscape(uno);
        user.setUno(uno);
        user.setActive_status(1);
        user.setCreateBy(create_by);

        if (username.equals("") || password.equals("")) { return 0; }
        boolean exist = isExist(username);
        if (exist) { return 2; }
        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        //String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        String encodedPassword = Pbkdf2Sha256.encode(password,salt);
        //  String encodedPassword_1 = new SimpleHash("SHA-256",password,salt,10);
        user.setSalt(salt);
        //  user.setUser_password(new Sha256Hash(password,null,10).toHex());
        user.setUser_password(encodedPassword);
        userDAO.save(user);
        return 1;
    }
    @Override
    public int msgregister(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getUser_password();
        String create_by = user.getCreateBy();
        Date now= new Date();
        Long createtime = now.getTime();
        Long updatetime = now.getTime();
        user.setCreateTime(createtime);
        user.setUpdateTime(updatetime);
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(username);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setActive_status(1);
        user.setCreateBy(create_by);

        if (username.equals("") || password.equals("")) { return 0; }
        boolean exist = isExist(username);
        if (exist) { return 2; }
        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        //String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        String encodedPassword = Pbkdf2Sha256.encode(password,salt);
        //  String encodedPassword_1 = new SimpleHash("SHA-256",password,salt,10);
        user.setSalt(salt);
        //  user.setUser_password(new Sha256Hash(password,null,10).toHex());
        user.setUser_password(encodedPassword);
        try {
            userDAO.save(user);
            int id = userDAO.findByUsername(username).getUId();
            adminUserRoleService.saveRoleChanges(id, user.getRoles());
        } catch (IllegalArgumentException e) {
            return 3;
        }
        return 1;
    }
    @Override
    public int resetpwd(String usrname,String pwd,String newpwd){
        Object usernamein = redisService.get("PWRD:"+pwd);
        if (usernamein==null){return -1;}
        if (!usernamein.toString().equals(usrname)){return -2;}
        User userInDB = userDAO.findByUsername(pwd);
        redisService.del("PWRD:"+pwd);
        try {
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2;
            String encodedPassword = Pbkdf2Sha256.encode(newpwd,salt);
            userInDB.setSalt(salt);
            userInDB.setUser_password(encodedPassword);
            userDAO.save(userInDB);
            return 1;
        }catch (Exception ex){
            return 3;
        }

    }
    @Override
    public String getusernamebysu(){
        Pac4jPrincipal p = SecurityUtils.getSubject().getPrincipals().oneByType(Pac4jPrincipal.class);
        CommonProfile profile = p.getProfile();
        String username = profile.getId();
        return username;
    }

    @Override
    public int unoBind(String phone, String code, String uno) {
        String username = getusernamebysu();
        if (username.equals(null)){return -1;}
        User user = findByUsername(username);
        if (!user.getPhone().equals(phone)){
            return 2;
        }
        //if (validUtil.validphone(phone, code)==false){return 3;}
        user.setUno(uno);
        try {
            userDAO.save(user);
            return 1;
        }catch (Exception e)
        {return -2;}
    }

    @Override
    public User geyuserbytoken() {
        return null;
    }

    @Override
    public String sendmailsecode(String mail) {
        String code = RandomUtil.generateDigitalString(6);
        redisService.set("mailcode:"+mail, code, 300);
        return code;
    }

    @Override
    public String sengmailvalidurl(String username) {
        String url = RandomUtil.generateString(16);
        redisService.set("PWRD:"+username, url, 300);
        return url;
    }

    @Override
    public boolean checkmailcode(String mail, String code) {
        if (!redisService.hasKey("mailcode:"+mail)){return false;}
        Object codeInDB = redisService.get("mailcode:"+mail).toString();
        if (code.equals(codeInDB)){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public String getUnoByUsername(String username) {
        return userDAO.findUnoByUsername(username);
    }
}
