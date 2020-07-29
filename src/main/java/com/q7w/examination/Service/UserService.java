package com.q7w.examination.Service;

import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.Uesr.AdminRole;

import com.q7w.examination.entity.User;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.util.Pbkdf2Sha256;

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
import java.util.Map;
import java.util.stream.Collectors;


public interface UserService {
    public List<UserDTO> list();
    public boolean resetPassword(User user);
    public boolean changePassword(String oldpwd,String pwd);
    public boolean usernamemailcheck(String username,String email);//用户名邮箱是否匹配
    public boolean isExist(String username);
    public String usernametouno(String username);
    public boolean isable(String username);
    public boolean wxisable(String wxuid);
    public boolean wxisExist(String wxuid);
    public boolean deleteUser(User user);
    public User findByUsername(String username);
    public User findByWx_uid(String wxuid);
    public void addOrUpdate(User user);
    public boolean updateUserStatus(User user);
    public boolean editUser(User user);
    public int unoBind(String phone,String code,String uno);
    public int register(User user);
    public int msgregister(User user);
    public int resetpwd(String usrname,String pwd,String newpwd);
    public String getusernamebysu();
    public User geyuserbytoken();
    public String sendmailsecode(String mail);
    public String sengmailvalidurl(String username);
    public boolean checkmailcode(String mail,String code);
    public String getUnoByUsername(String username);



}




