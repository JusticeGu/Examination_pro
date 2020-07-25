package com.q7w.examination.Service.impl;


import com.q7w.examination.Service.DataVisualizationService;
import com.q7w.examination.dao.ExamdataDAO;
import com.q7w.examination.dao.ExroomDAO;
import com.q7w.examination.dao.PaperDAO;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author JunXxxxi
 * @date 2020/7/20 18:10
 **/

@Service
public class DataVisualizationServiceimpl implements DataVisualizationService {
    @Autowired
    ExroomDAO exroomDAO;
    @Autowired
    ExamdataDAO examedataDAO;
    @Autowired
    PaperDAO paperDAO;
    @Autowired
    UserDAO userDAO;

    @Override
    public List<Integer> getNumOfExam() {
        Date now= new Date();
        Long current = now.getTime();
        Long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        Long t = zero;
        List<Integer> numOfExam = new ArrayList<>();
        for (int i=0;i<7;i++){
            t = t - i * 24*60*60*1000;
            numOfExam.add(exroomDAO.getNumExamPerDay(t));
        }
        return numOfExam;
    }

    @Override
    public List<Integer> getNumOfStudents(int kid) {
        List<Integer> dataList = new ArrayList<Integer>();
        Integer a = examedataDAO.countByKid(kid);
        dataList.add(a);
        Integer b = examedataDAO.countPass(kid);
        dataList.add(b);
        dataList.add(a-b);
        dataList.add(examedataDAO.countExcellent(kid));
        int i = examedataDAO.sumOfScore(kid);
        int j = a;
        i = i/j;
        Integer c = i;
        dataList.add(c);
        dataList.add(examedataDAO.maxOfScore(kid));
        dataList.add(examedataDAO.minOfScore(kid));
        return dataList;
    }

    @Override
    public List<Integer> getDisOfScore(int kid) {
        List<Integer> dataList = new ArrayList<Integer>();
        dataList.add(examedataDAO.countExcellent(kid));
        dataList.add(examedataDAO.countGood(kid));
        dataList.add(examedataDAO.countBad(kid));
        dataList.add(examedataDAO.countPoor(kid));
        return dataList;
    }

    @Override
    public Map<Integer, Integer> getWrongSituation(int kid) {
        List<Integer> dataList = new ArrayList<Integer>();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int pid = examedataDAO.getPid(kid);
        int n = paperDAO.queryNumOfQueByPid(pid);
        int m = 0, index = 0;
        for(int i=1;i<=n;i++){
            dataList.add(examedataDAO.getNumOfWrong(kid,i));
        }
        for(int i=0;i<4;i++){
            for(int j=0;j<n;j++){
                if(dataList.get(j)>m){
                    m = dataList.get(j);
                    index = j;
                }
            }
            map.put(index+1,m);
            dataList.set(index, -1);
            m = -1;
        }
        return map;
    }

    @Override
    public Map<Integer,Double> getRiaghtRate(int kid) {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        int pid = examedataDAO.getPid(kid);
        int n = paperDAO.queryNumOfQueByPid(pid);
        int numOfStudents = examedataDAO.countByKid(kid);
        for(int i=1;i<=n;i++){
            double m = 1-examedataDAO.getNumOfWrong(kid,i)*1.0/(numOfStudents*1.0);
            Double mm = Double.valueOf(String.format("%.2f", m));
            map.put(i,mm);
        }
        return map;
    }

    @Override
    public Map getStudentsList(int kid) {
        Map<String, Object> studentsList = new HashMap();
        List<Float> scoreList;
        scoreList = examedataDAO.findTotalscoreByKid(kid);
        List<String> unoList;
        unoList = examedataDAO.findUnoByKid(kid);
        for(int i=0;i<scoreList.size();i++){
            studentsList.put("name",userDAO.findNameByUno(unoList.get(i)));
            studentsList.put("uno",unoList.get(i));
            studentsList.put("totalscore",scoreList.get(i));
        }
        return studentsList;
    }
}
