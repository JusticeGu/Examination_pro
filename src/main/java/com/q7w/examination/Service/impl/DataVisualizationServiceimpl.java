package com.q7w.examination.Service.impl;


import com.q7w.examination.Service.DataVisualizationService;
import com.q7w.examination.dao.ExroomDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author JunXxxxi
 * @date 2020/7/20 18:10
 **/

@Service
public class DataVisualizationServiceimpl implements DataVisualizationService {
    @Autowired
    ExroomDAO exroomDAO;

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
}
