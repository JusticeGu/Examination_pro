package com.q7w.examination.Service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.q7w.examination.Service.CourseService;
import com.q7w.examination.Service.PaperService;
import com.q7w.examination.Service.QuestionsService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dao.PaperDAO;
import com.q7w.examination.dao.QuestionsDAO;
import com.q7w.examination.dto.QuestionsDTO;
import com.q7w.examination.entity.Examdata;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@Service
public class QuestionsServiceimpl implements QuestionsService {
    @Autowired
    QuestionsDAO questionsDAO;
    @Autowired
    PaperDAO paperDAO;
    @Autowired
    UserService userService;
    @Autowired
    PaperService paperService;
    @Autowired
    QuestionsService questionsService;
    @Autowired
    CourseService courseService;
    @Override
    public List<Questions> list() {
        List<Questions> list = questionsDAO.findAll();
        return list;
    }

    @Override
    public List<Questions> listbycourse(int cid) {
        List<Questions> list = questionsDAO.findAllByCid(cid);
        return list;
    }

    @Override
    public int addquestion(Questions questions) {
        Date now= new Date();
        Long createtime = now.getTime();
        questions.setCreateTime(createtime);
        questions.setUpdateTime(createtime);
     //   String username = userService.getusernamebysu();
        questions.setCreateBy("sys");
  //      if (!courseService.isexist(questions.getCid())){return 3;}
        try {
            questionsDAO.save(questions);
            return 1;
        } catch (IllegalArgumentException e) {
            return 2;
        }
    }

    @Override
    public int modifyquestion(Questions questions) {
        Questions questionsInDB = questionsDAO.findByQid(questions.getQid());
        if (questionsInDB.equals(null)){return -1;}
        questionsInDB.setQuestionName(questions.getQuestionName());
        questionsInDB.setAnswer(questions.getAnswer());
        questionsInDB.setContext(questions.getContext());
        questionsInDB.setType(questions.getType());
        questionsInDB.setCid(questions.getCid());
        questionsInDB.setDiffcult(questions.getDiffcult());
        questionsInDB.setRemarks(questions.getRemarks());
        questionsInDB.setOptionA(questions.getOptionA());
        questionsInDB.setOptionB(questions.getOptionB());
        questionsInDB.setOptionC(questions.getOptionC());
        questionsInDB.setOptionD(questions.getOptionD());
        questionsInDB.setOptionE(questions.getOptionE());
        questionsInDB.setOptionF(questions.getOptionF());
        questionsInDB.setLastmodifiedBy("user");
        Date now= new Date();
        Long modifytime = now.getTime();
        questionsInDB.setUpdateTime(modifytime);
        try{
            questionsDAO.save(questionsInDB);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }

    }

    @Override
    public int uploadquestion(MultipartFile multipartFile) {
        try {
            File file = FileUtil.toFile(multipartFile);
            // 使用 ExcelUtil 读取 Excel 中的数据
            ExcelReader reader = ExcelUtil.getReader(file);
            // 输出到 Question 的 List 集合中
            List<Map<String,Object>> questions = reader.readAll();
            // 手动删除临时文件
            if (!multipartFile.isEmpty()) {
                file.deleteOnExit();
            }

            for (Map<String,Object> question : questions) {
                // 正确答案、难度、所属课程、类型 ID 检测

                // 同步导入
                synchronized (this) {

                    Questions newquestions = new Questions();
                    newquestions.setAnswer(question.get("答案").toString());
                        // 插入题目数据
                      //  this.questionsDAO.save(newquestions);
                    System.out.println(question.get("问题主体"));

                }
            }
        } catch (Exception e) {
            // 捕捉所有可能发生的异常，抛出给控制层处理
           // log.error(ExceptionUtil.stacktraceToString(e));
            return 2;
        }
        return 1;
    }

    @Override
    public int delquestion(int qid) {
        try{
        questionsDAO.deleteById(qid);
        return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }


    @Override
    public List<Questions> getbycidtypediff(int cid, int type, int diff) {
        return null;
    }

    @Override
    public Questions getquestionbyid(int qid) {
        return questionsDAO.findByQid(qid);
    }

    @Override
    public String findquestion() {
        return null;
    }

    @Override
    public List<Questions> listbytype(int type) {

        return questionsDAO.findAllByType(type);
    }

    @Override
    public List<Questions> listbytypeandcid(int type, int cid) {
        return null;
    }

    @Override
    public List<Questions> getquestionbypid(int pid) {
        // 通过 ID 查询试卷信息
        Paper paper = this.paperDAO.findByPid(pid);
        // 获取试卷的题目序号集合，Example:（1,2,3,4,5,6,7）
        String qIds = paper.getQuestionId();
        // 分割题目序号
        String[] ids = StrUtil.splitToArray(qIds, StrUtil.C_COMMA);
        List<Questions> questionlist = Lists.newArrayList();
        // 遍历试题 ID，找出对应类型 ID 的问题并加入 Set 集合当中
        for (String id : ids) {
            // 通过题目 ID 获取问题的信息
            Questions questions = questionsDAO.findByQid(Integer.parseInt(id));
            // 类型id为空或者类型id与当前题目的类型一致
            questionlist.add(questions);
        }
        return questionlist;
    }
}
