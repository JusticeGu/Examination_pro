package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.ExroomService;
import com.q7w.examination.Service.PaperService;
import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dto.AnsmarkDTO;
import com.q7w.examination.entity.Examdata;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaperServiceimpl implements PaperService {
    @Autowired
    UserService userService;
    @Autowired
    QuestionsService questionsService;
    @Autowired
    PaperService paperService;
    @Autowired
    ExroomService exroomService;

    @Override
    public int listPaper() {
        return 0;
    }

    @Override
    public int addqPaper(Paper paper) {
        return 0;
    }

    @Override
    public boolean isExist(String papername) {
        return false;
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

    /**
     * 阅卷服务
     *
     * @param uid     用户id
     * @param pid     试卷号
     * @param request 前端请求(答案列表)
     * @return
     */
    @Override
    public List<Map<String, Object>> markscore(int uid,int pid,int kid,HttpServletRequest request) {
        //获取试卷信息
        Paper paper = findPaperbyid(pid);
        // 获取模板各个题型的题目分值
        float singleScore = paper.getSinscore();
        float mulScore = paper.getMulscore();
        float subfScore = paper.getSubscore();
        // 错题集
        List<String> wrongIds = new ArrayList<>();
        // 定义默认分值
        int score = 0;
        /* -------------------------- 开始评分 -------------------------- */
        // 加同步锁
        synchronized (this) {
            // 获取试问题列表
            List<Questions> questions = questionsService.getquestionbypid(pid);
            // 按照题目类型分组
            Collection<List<Questions>> collection = questions.stream()
                    .sorted(Comparator.comparingInt(Questions::getType))
                    .collect(Collectors.groupingBy(Questions::getType)).values();
            for (List<Questions> questionList : collection) {
                switch (questionList.get(0).getType()) {
                    case (1):
                        // 单选题批改
                        AnsmarkDTO choiceMark = ScoreUtil.mark(questionList, singleScore, request);
                        score += choiceMark.getScore();
                        wrongIds.addAll(choiceMark.getWrongIds());
                        break;
                    case (2):
                        // 多选题批改
                        AnsmarkDTO mulChoiceMark = ScoreUtil
                                .mulMark(questionList, mulScore, request);
                        score += mulChoiceMark.getScore();
                        wrongIds.addAll(mulChoiceMark.getWrongIds());
                        break;
                }
            }
        }
        /* -------------------------- 结束评分 -------------------------- */

        // 组装错题集合信息
        StringBuilder builder = new StringBuilder();
        for (String id : wrongIds) {
            builder.append(id).append(',');
        }
        // 最后一个逗号去除
        String wrong = builder.toString();
        // 预备一个空错题字符串
        String wrongStr = null;
        // 如果没有错题，就直赋值空，长度大于0就说明包含错题
        if (wrong.length() > 0) {
            wrongStr = wrong.substring(0, wrong.length() - 1);
        }

        // 封装分数参数，并将分数信息插入到分数表中
       // Examdata examdata = new Examdata(uid, pid,kid, String.valueOf(score), wrongStr);
        // 此处调用插入接口
    //    this.scoreService.save(scoreResult);
        return null;
    }

    @Override
    public List<String> stringToList(String strs) {
        return null;
    }
}