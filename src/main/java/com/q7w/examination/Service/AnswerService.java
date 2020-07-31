package com.q7w.examination.Service;

import com.q7w.examination.entity.Answer;

import java.util.List;
import java.util.Map;

/**
 * @author JunXxxxi
 * @date 2020/7/30 12:29
 **/
public interface AnswerService {
    public int saveAnswer(Answer answer);
    public Map getAnswer(int eid);
    public int upadteAnswer(int eid,int qid,String anscontent);
    public List<Integer> getQidListByEid(int eid);
}
