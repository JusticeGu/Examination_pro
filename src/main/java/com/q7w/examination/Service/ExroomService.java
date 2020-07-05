package com.q7w.examination.Service;

import com.q7w.examination.entity.ExamSession;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ExroomService {
    public int listExroom();
    public int addExroom(Exroom exroom);
    public int modifyExroom(Exroom exroom);
    public int delExroom(int kid);
    public Exroom findExroom(int kid);
    public int isExist(int kid);
    public List<Map<String, Object>> getExroomList();
    public int enterExroom(int kid);
    public List<String> stringToList(String strs);
    public int submitexam(ExamSession examSession);
}
