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
    public int addexamdata(int kid,int pid,String uno) {
        Examdata examdata = new Examdata();
        examdata.setKid(kid);
        examdata.setPid(pid);
        examdata.setUno(uno);
        Date now= new Date();
        Long createtime = now.getTime();
        examdata.setCreateTime(createtime);
        try{
            examdataDAO.save(examdata);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }

    }

    @Override
    public int updateexamdata(int kid, int pid, String uno, Map ans, String score, String wronglist) {
        Examdata examdata = examdataDAO.findByKidAndPidAndUno(kid, pid,uno);
        if (examdata==null){return -1; }
        examdata.setAnslist(ans.toString());
        examdata.setObjscore(score);
        examdata.setWronglist(wronglist);
        Date now= new Date();
        Long createtime = now.getTime();
        examdata.setUpdateTime(createtime);
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
