package com.q7w.examination.Service;

import com.q7w.examination.entity.Examdata;

import java.util.List;
import java.util.Map;

/**
 * @author xiaogu
 * @date 2020/7/17 17:21
 **/
public interface ExamDataService {
    public int addexamdata(int kid,int pid,String uno,int ltimes);
    public Examdata getexam(int kid,int pid,String uno);
    public boolean delexamdata(int eid);
    public int updateexamdata(int kid, int pid, String uno, Map<String,String> ans,float score,String wronglist);
    public int modifydata(Examdata examdata);
    public List<Examdata> querydatabyuno();
    public List<Examdata>  querydatabykid(int kid);
    public Map getExamResult(int kid);
    public Map getTExamResult(int kid, String uno);
}
