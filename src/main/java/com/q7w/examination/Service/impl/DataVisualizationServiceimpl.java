package com.q7w.examination.Service.impl;


import com.q7w.examination.Service.*;
import com.q7w.examination.dao.ExamdataDAO;
import com.q7w.examination.dao.ExroomDAO;
import com.q7w.examination.dao.PaperDAO;
import com.q7w.examination.dao.UserDAO;
import com.q7w.examination.entity.Questions;
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
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionsService questionsService;
    @Autowired
    AnswerService answerService;

    @Override
    public List<Integer> getNumOfExam() {
        if(redisService.get("NumOfExam")==null){
            String name = userService.getusernamebysu();
            Date now= new Date();
            Long current = now.getTime();
            Long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
            Long t = zero;
            List<Integer> numOfExam = new ArrayList<>();
            t = t - 6 * 24*60*60*1000;
            for (int i=0;i<7;i++){
                numOfExam.add(exroomDAO.getNumExamPerDay(t,name));
                t = t + 24*60*60*1000;
            }
            redisService.set("NumOfExam",numOfExam,1800);
            return numOfExam;
        }
        else {
            return (List<Integer>) redisService.get("NumOfExam");
        }
    }

    @Override
    public List<Integer> getNumOfStudents(int kid) {
        if(redisService.get("NumOfStudents")==null){
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
            redisService.set("NumOfStudents",dataList,1800);
            return dataList;
        }
        else {
            return (List<Integer>) redisService.get("NumOfStudents");
        }
    }

    @Override
    public List<Integer> getDisOfScore(int kid) {
        if(redisService.get("DisOfScore")==null){
            List<Integer> dataList = new ArrayList<Integer>();
            dataList.add(examedataDAO.countExcellent(kid));
            dataList.add(examedataDAO.countGood(kid));
            dataList.add(examedataDAO.countBad(kid));
            dataList.add(examedataDAO.countPoor(kid));
            redisService.set("DisOfScore",dataList,1800);
            return dataList;
        }
        else {
            return (List<Integer>) redisService.get("DisOfScore");
        }
    }

    @Override
    public Map<String, Object> getWrongSituation(int kid) {
        if(redisService.get("WrongSituation")==null){
            List<Integer> dataList = new ArrayList<Integer>();
            Map<String, Object> map = new HashMap<String, Object>();
            List<Integer> numList = new ArrayList<>();
            List<Integer> wrongQidList = new ArrayList<>();
            int m = 0, index = 0,qid = 0;

            int eid = examedataDAO.findFirstEidByKid(kid);
            List<Integer> qidList = answerService.getQidListByEid(eid);
            for(int i=0;i<qidList.size();i++){
                dataList.add(examedataDAO.getNumOfWrong(kid,qidList.get(i)));
            }

            for(int i=0;i<4;i++){
                for(int j=0;j<qidList.size();j++){
                    if(dataList.get(j)>m){
                        m = dataList.get(j);
                        qid = qidList.get(j);
                        index = j;
                    }
                }
                wrongQidList.add(answerService.getVisualQidByQidAndEid(qid,eid));
                numList.add(m);
                dataList.set(index, -1);
                m = -1;
            }
            map.put("wrongqidList",wrongQidList);
            map.put("numList",numList);
            redisService.set("WrongSituation",map,1800);
            return map;
        }
        else {
            return (Map<String, Object>) redisService.get("WrongSituation");
        }
    }

    @Override
    public Map<String,Object> getRiaghtRate(int kid) {
        if(redisService.get("RiaghtRate")==null){
            Map<String, Object> map = new HashMap<String, Object>();
            int eid = examedataDAO.findFirstEidByKid(kid);
            List<Integer> qidList = answerService.getQidListByEid(eid);
            List<Double> rightRateList = new ArrayList<>();
            List<Integer> v = new ArrayList<>();

            int numOfStudents = examedataDAO.countByKid(kid);
            for(int i=0;i<qidList.size();i++){
                double m = 1-examedataDAO.getNumOfWrong(kid,qidList.get(i))*1.0/(numOfStudents*1.0);
                Double mm = Double.valueOf(String.format("%.2f", m));
                rightRateList.add(mm);
                v.add(answerService.getVisualQidByQidAndEid(qidList.get(i),eid));
            }
            map.put("qidList",v);
            map.put("rightRateList",rightRateList);
            redisService.set("RiaghtRate",map,1800);
            return map;
        }
        else {
            return (Map<String, Object>) redisService.get("RiaghtRate");
        }

    }

    @Override
    public List<Map> getStudentsList(int kid) {
        if(redisService.get("StudentsList")==null){
            List<Map> studentsList = new ArrayList<>();
            List<String> unoList;
            unoList = examedataDAO.findUnoByKid(kid);
            for(int i=0;i<unoList.size();i++){
                Map<String, Object> student = new HashMap();
                student.put("name",userDAO.findNameByUno(unoList.get(i)));
                student.put("uno",unoList.get(i));
                student.put("totalscore",examedataDAO.findTotalscoreByKidAndUno(kid,unoList.get(i)));
                studentsList.add(student);
            }
            redisService.set("StudentsList",studentsList,1800);
            return studentsList;
        }
        else {
            return (List<Map>) redisService.get("StudentsList");
        }
    }

    @Override
    public boolean addwronglist(int qid) {
        try{
            double score = redisService.sortSetZincrby("wronglisttop",Integer.toString(qid),1 );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List wrongtop(int range) {
        if (redisService.get("Wrongtop")==null){
        Set qidset=redisService.sortSetRange("wronglisttop",0,range-1);
        List<String> questionsList = new ArrayList<>();
        for (Object qid:qidset){
            questionsList.add(questionsService.getquestionbyid(Integer.parseInt(qid.toString())).getQuestionName());
        }
        redisService.set("Wrongtop",questionsList,3600*27*7);
        return questionsList;}
        else {
            return (List)redisService.get("Wrongtop");
        }
    }

    @Override
    public Map getDashboard() {
        if(redisService.get("Dashboard")==null){
            String name = userService.getusernamebysu();
            Date now= new Date();
            Long current = now.getTime();
            Long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
            Long t = zero;
            Map<String, Object> dashboard = new HashMap();
            dashboard.put("发布考试",exroomDAO.getNumExamPerMonth(t,name));
            dashboard.put("未开始考试",exroomDAO.getNumNotStart(t,current,name));
            dashboard.put("已开始/已结束考试",exroomDAO.getNumExamPerMonth(t,name)-exroomDAO.getNumNotStart(t,current,name));
            dashboard.put("添加试卷",paperDAO.getNumPaperPerMonth(t,name));
            redisService.set("Dashboard",dashboard,1800);
            return dashboard;
        }
        else {
            return (Map) redisService.get("Dashboard");
        }
    }

    @Override
    public Map getSDashboard() {
        if(redisService.get("SDashboard")==null){
            String name = userService.getusernamebysu();
            String uno = redisService.hmget("TK:"+name).get("uno").toString();
            Map<String, Object> dashboard = new HashMap();
            dashboard.put("参加考试",examedataDAO.countByUno(uno));
            if(examedataDAO.countByUno(uno)==0){
                dashboard.put("平均分",0);
            }
            else {
                dashboard.put("平均分", examedataDAO.sumOfScoreByUno(uno) / examedataDAO.countByUno(uno));
            }
            if(examedataDAO.maxOfScoreByUno(uno)==null){
                dashboard.put("最高分",0);
            }
            else {
                dashboard.put("最高分",examedataDAO.maxOfScoreByUno(uno));
            }
            if (examedataDAO.minOfScoreByUno(uno)==null){
                dashboard.put("最低分",0);
            }
            else {
                dashboard.put("最低分",examedataDAO.minOfScoreByUno(uno));
            }
            redisService.set("SDashboard",dashboard,1800);
            return dashboard;
        }
        else {
            return (Map) redisService.get("SDashboard");
        }
    }

    @Override
    public List<Integer> getNumOfSExam() {
        if(redisService.get("NumOfSExam")==null){
            String name = userService.getusernamebysu();
            String uno = redisService.hmget("TK:"+name).get("uno").toString();
            Date now= new Date();
            Long current = now.getTime();
            Long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
            Long t = zero;
            List<Integer> numOfExam = new ArrayList<>();
            t = t - 6 * 24*60*60*1000;
            for (int i=0;i<7;i++){
                numOfExam.add(examedataDAO.getNumSExamPerDay(t,uno));
                t = t + 24*60*60*1000;
            }
            redisService.set("NumOfSExam",numOfExam,1800);
            return numOfExam;
        }
        else {
            return (List<Integer>) redisService.get("NumOfSExam");
        }
    }


}
