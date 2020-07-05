package com.q7w.examination.Service;
import com.q7w.examination.dto.QuestionsDTO;
import com.q7w.examination.entity.Examdata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface QuestionsService {
    public int listquestion();
    public int addquestion();
    public int modifyquestion();
    public int delquestion();
    public String findquestion();
    public List<Map<String, Object>> getquestions(int sin,int mul,int sub,int type);//抽取试卷
    public List<QuestionsDTO> getquestionsdto(int sin, int mul, int sub, int type);//抽取试卷
    public List<Map<String, Object>> checksinans(Map<String, Object> map);//单选阅卷
    public List<Map<String, Object>> checksinans_bj(String ans);//单选阅卷
    public List<Map<String, Object>> checkmulans(Map<String, Object> map);//多选阅卷
    public List<Map<String, Object>> checksubans(Map<String, Object> map);//主观阅卷
    public int createscore(Examdata examdata);//合成分数

}
