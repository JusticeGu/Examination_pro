package com.q7w.examination.Service;


import com.q7w.examination.dto.PaperDTO;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PaperService {
    public List<Paper> listPaper();
    public Page<Paper> listpapersbynum(Pageable pageable);
    public List<PaperDTO> querypaper(String coursename);
    public int addqPaper(Paper paper);
    public boolean isExist(String papername);
    public int modifyPaper(Paper paper);
    public int delPaper(int pid);
    public Paper findPaperbyid(int pid);
    public boolean isExist(int pid);
    public Map getPaperInfo(int pid);
    public List<Questions> getPaperQuestionList(int pid);
    public Map submitpaper(int kid,int pid,HttpServletRequest request,Map ansmap);
    public List<String> stringToList(String strs);
    public Map<String, Object> markscore( int pid, Map ansmap);//阅卷服务
    public float getTotalScore(int pid);
}
