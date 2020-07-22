package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.ExamDataService;
import com.q7w.examination.dao.ExamdataDAO;
import com.q7w.examination.dao.ExroomDAO;
import com.q7w.examination.entity.Examdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xiaogu
 * @date 2020/7/17 17:23
 **/
@Service
public class ExamDataServiceimpl implements ExamDataService {
    @Autowired
    ExamdataDAO examdataDAO;
    @Override
    public int addexamdata(int kid,int pid,String uno,int ltimes) {
        Examdata examdata = getexam(kid, pid, uno);
        int times = examdata.getTimes();
        if (times>=ltimes&&ltimes!=0){return -1;}
        if (times==0){
            examdata.setTimes(1);
        }else {
            examdata.setTimes(times+1);
        }
        Date now= new Date();
        Long createtime = now.getTime();
        examdata.setKid(kid);
        examdata.setPid(pid);
        examdata.setUno(uno);
        examdata.setCreateTime(createtime);
        examdata.setStatus(1);
        try{
            examdataDAO.save(examdata);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }

    }

    @Override
    public Examdata getexam(int kid,int pid,String uno) {
        Examdata examdata = examdataDAO.findByKidAndPidAndUno(kid, pid, uno);
        if(examdata==null){return new Examdata();}
        else {
            return examdata;
        }
    }



    @Override
    public int updateexamdata(int kid, int pid, String uno, Map ans, float score, String wronglist) {
        Examdata examdata = examdataDAO.findByKidAndPidAndUno(kid, pid,uno);
        if (examdata==null){return -1; }
        examdata.setAnslist(ans.toString());
        if (examdata.getSubscore()<=score){
            examdata.setSubscore(score);
            examdata.setWronglist(wronglist);
        }
        Date now= new Date();
        Long createtime = now.getTime();
        examdata.setUpdateTime(createtime);
        examdata.setStatus(3);
        try{
            examdataDAO.save(examdata);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }

    @Override
    public boolean delexamdata(int eid) {
        examdataDAO.deleteById(eid);
        return true;
    }

    @Override
    public int modifydata(Examdata examdata) {
        return 0;
    }

    @Override
    public List<Examdata> querydatabyuno(String uno) {
        return examdataDAO.findAllByUno(uno);
    }

    @Override
    public List<Examdata>  querydatabykid(int kid) {
        return examdataDAO.findAllByKid(kid);
    }
}
