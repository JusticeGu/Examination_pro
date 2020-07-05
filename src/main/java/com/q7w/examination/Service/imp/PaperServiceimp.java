package com.q7w.examination.Service.imp;

import com.q7w.examination.Service.PaperService;
import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaperServiceimp implements PaperService {
    @Autowired
    UserService userService;
    @Autowired
    QuestionsService questionsService;
    @Autowired
    PaperService paperService;
    @Override
    public int listPaper() {
        return 0;
    }

    @Override
    public int addqPaper(Paper paper) {
        return 0;
    }

    @Override
    public int modifyPaper(Paper paper) {
        return 0;
    }

    @Override
    public int delPaper(int pid) {
        return 0;
    }

    @Override
    public Paper findPaperbyid(int pid) {
        return null;
    }


    @Override
    public int isExist(int pid) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> getPaperList() {
        return null;
    }

    @Override
    public List<Map<String, Object>> createPaper(int pid) {
        Paper paper = paperService.findPaperbyid(pid);
        if (paper.getType() == 1) {
         //   List<Questions> questoonList = new ArrayList<Questions>(questionsService.getquestions(1,1,1,1));
        }
        return null;
    }

    @Override
    public List<String> stringToList(String strs) {
        return null;
    }
}
