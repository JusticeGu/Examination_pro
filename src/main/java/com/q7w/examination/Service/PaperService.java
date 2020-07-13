package com.q7w.examination.Service;

import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.Paper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PaperService {
    public int listPaper();
    public int addqPaper(Paper paper);
    public boolean isExist(String papername);
    public int modifyPaper(Paper paper);
    public int delPaper(int pid);
    public Paper findPaperbyid(int pid);
    public int isExist(int pid);
    public List<Map<String, Object>> getPaperList();
    public List<Map<String, Object>> createPaper(int pid);
    public List<String> stringToList(String strs);
    public List<Map<String, Object>> markscore(int uid,int pid,int kid,HttpServletRequest request);//阅卷服务
}
