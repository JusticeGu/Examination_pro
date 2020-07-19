package com.q7w.examination.Service;

import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PaperService {
    public List<Paper> listPaper();
    public int addqPaper(Paper paper);
    public boolean isExist(String papername);
    public int modifyPaper(Paper paper);
    public int delPaper(int pid);
    public Paper findPaperbyid(int pid);
    public boolean isExist(int pid);
    public Map getPaperInfo(int pid);
    public List<Questions> getPaperList(int pid);
    public Map submitpaper(int kid,int pid,HttpServletRequest request,Map ansmap);
    public List<String> stringToList(String strs);
    public Map<String, Object> markscore(int uid, int pid, int kid, Map ansmap);//阅卷服务
}
