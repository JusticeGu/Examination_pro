package com.q7w.examination.Service.imp;

import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.dto.QuestionsDTO;
import com.q7w.examination.entity.Examdata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionsServiceimp implements QuestionsService {
    @Override
    public int listquestion() {
        return 0;
    }

    @Override
    public int addquestion() {
        return 0;
    }

    @Override
    public int modifyquestion() {
        return 0;
    }

    @Override
    public int delquestion() {
        return 0;
    }

    @Override
    public String findquestion() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getquestions(int sin, int mul, int sub, int type) {
        return null;
    }

    @Override
    public List<QuestionsDTO> getquestionsdto(int sin, int mul, int sub, int type) {
        return null;
    }

    @Override
    public List<Map<String, Object>> checksinans(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<Map<String, Object>> checksinans_bj(String ans) {
        return null;
    }

    @Override
    public List<Map<String, Object>> checkmulans(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<Map<String, Object>> checksubans(Map<String, Object> map) {
        return null;
    }

    @Override
    public int createscore(Examdata examdata) {
        return 0;
    }
}
