package com.q7w.examination.Service;

import com.q7w.examination.entity.ExamSession;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ExroomService {
    public List<Exroom> listExroom();
    public int addExroom(Exroom exroom);
    public int modifyExroom(Exroom exroom);
    public int delExroom(int kid);
    public Exroom findExroom(int kid);
    public boolean isExist(int kid);
    public List<Map<String, Object>> getExroomList();
    public int enterExroom(int kid);
    public List<String> stringToList(String strs);
    public int submitexam(ExamSession examSession);
    public boolean putpermission(String exid,String uno);
    public boolean checkpermission(String exid, String uno);
    public int startexrooom(int kid);
    public int endroom(int kid);
    public int uploadgrouplist(MultipartFile multipartFile);
}
