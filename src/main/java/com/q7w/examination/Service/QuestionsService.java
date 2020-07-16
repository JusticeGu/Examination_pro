package com.q7w.examination.Service;
import com.q7w.examination.dto.QuestionsDTO;
import com.q7w.examination.entity.Examdata;
import com.q7w.examination.entity.Questions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface QuestionsService {
    public List<Questions> list();
    public List<Questions> listbycourse(int cid);
    public List<Questions> listbytype(int type);
    public List<Questions> listbytypeandcid(int type,int cid);
    public int addquestion(Questions questions);
    public int modifyquestion(Questions questions);
    public int uploadquestion(MultipartFile multipartFile);
    public List<Questions> getbycidtypediff(int cid,int type,int diff);
    public Questions getquestionbyid(int qid);
    public int delquestion(int qid);
    public String findquestion();
    public List<Questions> getquestionbypid(int pid);



}
