package com.q7w.examination.Service;

import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.AdminRole;

import com.q7w.examination.entity.User;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import com.q7w.examination.util.HttpUtil;
import com.q7w.examination.util.Pbkdf2Sha256;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
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
    public boolean isExist(String username) {
        User user = userDAO.findByUsername(username);
        return null!=user;
    }
    public String usernametouno(String username){
        User user = userDAO.findByUsername(username);
        return user.getUno();
    }
    public boolean isable(String username) {
        User user = userDAO.findByUsername(username);
        boolean status = user.isEnabled();
        return status;
    }
    public boolean wxisable(String wxuid) {
        User user = userDAO.findByWxuid(wxuid);
        boolean status = user.isEnabled();
        return status;
    }
    public boolean wxisExist(String wxuid) {
        User user = userDAO.findByWxuid(wxuid);
        return null!=user;
    }
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
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    public User findByWx_uid(String wxuid) {
        return userDAO.findByWxuid(wxuid);
    }
    public void addOrUpdate(User user) {
        userDAO.save(user);
    }
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
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
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
    public int resetpwd(String usrname,String pwd,String newpwd){
        User userInDB = userDAO.findByUsername(usrname);
        String username = usrname;
        String password = pwd;
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
            subject.login(usernamePasswordToken);

        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return 2;
        }
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


}




