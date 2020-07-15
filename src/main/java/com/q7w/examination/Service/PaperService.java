package com.q7w.examination.Service;

import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PaperService {
    public List<Paper> listPaper();
    public int addqPaper(Paper paper);
    public boolean isExist(String papername);
    public int modifyPaper(Paper paper);
    public int delPaper(int pid);
    public Paper findPaperbyid(int pid);
    public boolean isExist(int pid);
    public Set<Questions> getPaperList(int pid);
    public int submitpaper(int kid,int pid,HttpServletRequest request);
    public List<Map<String, Object>> createPaper(int pid);
    public List<String> stringToList(String strs);
    public List<Map<String, Object>> markscore(int uid,int pid,int kid,HttpServletRequest request);//阅卷服务
}
