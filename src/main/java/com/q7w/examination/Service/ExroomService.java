package com.q7w.examination.Service;

import com.q7w.examination.entity.ExamSession;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ExroomService {
    public List<Exroom> listExroom();
    public Page<Exroom> listexroombynum(Pageable pageable);
    public int addExroom(Exroom exroom);
    public int modifyExroom(Exroom exroom);
    public int delExroom(int kid);
    public Exroom findExroom(int kid);
    public boolean isExist(int kid);
    public List<Map<String, Object>> getExroomList();
    public Map enterExroom(int kid,String username);
    public List<String> stringToList(String strs);
    public int submitexam(ExamSession examSession);
    public boolean putpermission(String exid,String uno);
    public boolean checkpermission(String exid, String uno);
    public int startexrooom(int kid);
    public int endroom(int kid);
    public int uploadgrouplist(MultipartFile multipartFile);
    public List<Exroom> getNotStartList();
    public List<Exroom> getStartedList();
    public List<Exroom> getLastThreeExam(String name);
    public List<Exroom> getSLastThreeExam();
}
