package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.AnswerService;
import com.q7w.examination.dao.AnswerDAO;
import com.q7w.examination.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JunXxxxi
 * @date 2020/7/30 12:29
 **/

@Service
public class AnswerServiceimpl implements AnswerService {
    @Autowired
    AnswerDAO answerDAO;
    @Override
    public int saveAnswer(Answer answer) {
        try{
            answerDAO.save(answer);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }

    @Override
    public Map getAnswer(int eid) {
        List<Integer> qidList;
        qidList = answerDAO.getQidByEid(eid);
        List<String> ansList;
        ansList = answerDAO.getAnsByEid(eid);
        Map<Integer, String> answer = new HashMap();
        for(int i=0;i<qidList.size();i++){
//            answer.put("questionID"+i,qidList.get(i));
            answer.put(qidList.get(i),ansList.get(i));
        }
        return answer;
    }

    @Override
    public int upadteAnswer(int eid, int qid, String anscontent) {
        try{
            answerDAO.updateAnswer(eid,qid,anscontent);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }
}
