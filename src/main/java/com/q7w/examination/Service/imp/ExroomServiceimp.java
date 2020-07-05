package com.q7w.examination.Service.imp;

import com.q7w.examination.Service.ExroomService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.ExamSession;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.RandomUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExroomServiceimp implements ExroomService {
    @Autowired
    UserService userService;
    @Override
    public int enterExroom(int kid) {
        if(isExist(kid)==-1){return 0;}
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        if(username==null){return 3;}
        Exroom exroomInDB = findExroom(kid);
        String uno = userService.usernametouno(username);
        List unolist = stringToList(exroomInDB.getGroup());
        if(unolist.contains(uno)==false){return 3;}
        Date now= new Date();
        Long createtime = now.getTime();
        if (createtime>=exroomInDB.getDeadline()){return 2;}
        //逻辑
      //  int kno = RandomUtil.toFixdLengthString(uno+kid+RandomUtil.generateDigitalString(3), 16);
        return 1;
    }

    @Override
    public int isExist(int kid) {
        return 0;
    }
    @Override
    public List<String> stringToList(String strs){
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }
    @Override
    public Exroom findExroom(int kid) {
        return null;
    }

    @Override
    public int listExroom() {
        return 0;
    }

    @Override
    public int addExroom(Exroom exroom) {
        return 0;
    }

    @Override
    public int modifyExroom(Exroom exroom) {
        return 0;
    }

    @Override
    public int delExroom(int kid) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> getExroomList() {
        return null;
    }

    @Override
    public int submitexam(ExamSession examSession) {
        return 0;
    }
}
