package com.q7w.examination.Service;

import java.util.List;
import java.util.Map;

/**
 * @author JunXxxxi
 * @date 2020/7/20 18:10
 **/

public interface DataVisualizationService {
    public List<Integer> getNumOfExam();
    public List<Integer> getNumOfStudents(int kid);
    public List<Integer> getDisOfScore(int kid);
    public Map<Integer,Integer> getWrongSituation(int kid);
    public Map<Integer,Double> getRiaghtRate(int kid);
    public Map getStudentsList(int kid);
}
