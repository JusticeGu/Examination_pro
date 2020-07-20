package com.q7w.examination.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.q7w.examination.Service.*;
import com.q7w.examination.dao.PaperDAO;
import com.q7w.examination.dto.AnsmarkDTO;
import com.q7w.examination.dto.PaperDTO;
import com.q7w.examination.dto.QuestionsDTO;
import com.q7w.examination.dto.UserDTO;
import com.q7w.examination.entity.Paper;
import com.q7w.examination.entity.Questions;
import com.q7w.examination.entity.Uesr.AdminRole;
import com.q7w.examination.entity.User;
import com.q7w.examination.util.ScoreUtil;
import com.q7w.examination.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@Service
public class PaperServiceimpl implements PaperService {
    @Autowired
    UserService userService;
    @Autowired
    QuestionsService questionsService;
    @Autowired
    ExroomService exroomService;
    @Autowired
    RedisService redisService;
    @Autowired
    ExamDataService examDataService;
    @Autowired
    PaperDAO paperDAO;
    @Autowired
    TokenUtil tokenUtil;

    @Override
    public List<Paper> listPaper() {
        return paperDAO.findAll();
    }

    @Override
    public int addqPaper(Paper paper) {
        if (isExist(paper.getName())){return 0;}
        Date now= new Date();
        Long createtime = now.getTime();
        paper.setCreateTime(createtime);
        paper.setUpdateTime(createtime);
        paper.setCreateBy("user");
        //-------获取题目列表方法一
      //  List<Integer> qnumlist = paper.getQidList();
        //List<Questions> questionsList = questionsService.listallbyidset(qnumlist);
        //-------获取题目列表方法二
        String[] qulist = paper.getQuestionId().split(",");
        List<Questions> questionl = new ArrayList<>();
        for (int i=0;i<=qulist.length-1;i++){
            try {
                Questions questions = questionsService.getquestionbyid(Integer.parseInt(qulist[i]));
               // questions.
                questionl.add(questions);
                String ans = questions.getAnswer();
            }catch (Exception e){
                return 2;
            }
        }
        paper.setQucontent(JSONObject.toJSONString(questionl));
        try{
            paperDAO.save(paper);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }

    @Override
    public List<Questions> getPaperQuestionList(int pid) {
        Paper paper = findPaperbyid(pid);
        List<Questions> questionSet = JSONObject.parseObject(paper.getQucontent(),List.class);

        return questionSet;
    }

    @Override
    public Page<Paper> listpapersbynum(Pageable pageable) {
        return paperDAO.findAll(pageable);
    }

    @Override
    public List<PaperDTO> querypaper(String name) {
        List<Paper> papers =  paperDAO.findAllByNameLike(name);
        List<PaperDTO> paperDTOS = papers.stream().map(paper -> (PaperDTO) new PaperDTO().convertFrom(paper)).collect(Collectors.toList());
        return paperDTOS;
    }

    @Override
    public Map getPaperInfo(int pid) {
    //    Map paperinfo = redisService.hmget("psh-"+pid);
      //  if (paperinfo.size()!=0){
      //      return paperinfo;}
        Paper paper = findPaperbyid(pid);
        Map<String, Object> paperinfo = new HashMap();
        paperinfo.put("papername", paper.getName());
        paperinfo.put("score",paper.getSinscore());
        List<Questions> questionSet = JSONObject.parseObject(paper.getQucontent(),List.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", questionSet.size());
        map.put("questionList", questionSet);
        String json = JSON.toJSONString(map, true);
        List<Questions> questionList = JSON.parseArray(JSON.parseObject(json).getString("questionList"),Questions.class);
        List<QuestionsDTO> questionDTOS =questionList.stream().map(questions ->(QuestionsDTO) new QuestionsDTO().convertFrom(questions)).collect(Collectors.toList());
        Collections.shuffle(questionDTOS);
        paperinfo.put("questions",questionDTOS);
        redisService.hmset("psh-"+pid,paperinfo,3600);
       // long time = redisService.getExpire("exroom-"+kid)
        return paperinfo;

    }

    @Override
    public boolean isExist(String papername) {
        Paper paper=paperDAO.findByName(papername);
        return null!=paper;
    }

    @Override
    public int modifyPaper(Paper paper) {
        return 0;
    }

    @Override
    public int delPaper(int pid) {
        if (!isExist(pid)){return -1;}
        paperDAO.deleteById(pid);
        return 1;
    }

    @Override
    public Paper findPaperbyid(int pid) {
        Object paperdb = redisService.get("ppif-"+pid);
        if (paperdb==null){
            Paper paperdbdao = paperDAO.findByPid(pid);
            String json = JSON.toJSONString(paperdbdao, true);
            redisService.set("ppif-"+pid,json,3600);
            return paperdbdao;
        } else {
            Paper paper = JSONObject.parseObject(paperdb.toString(),Paper.class);
            return paper;
        }

    }


    @Override
    public boolean isExist(int pid) {
        Paper paper=paperDAO.findByPid(pid);
        return null!=paper;
    }



    @Override
    public Map submitpaper(int kid,int pid, HttpServletRequest request,Map ansmap) {

        Map infomsg = new HashMap();
    //    String username = tokenUtil.getusername(request);
   //     if (username==null){
 //           infomsg.put("code","0");
 //           return infomsg;}
 //       int uid = userService.findByUsername(username).getUId();
        Map markinfo=markscore(1, pid, kid,ansmap);
        System.out.println(markinfo);
        // int ans =  examDataService.updateexamdata(kid, pid, "uid",
        //      ansmap,markinfo.get("score").toString(),markinfo.get("wrong").toString())
    //    if(ans==-1){infomsg.put("code","200");return infomsg;}
        infomsg.put("code","200");
        infomsg.put("score",markinfo);

        return infomsg;
    }

    /**
     * 阅卷服务
     *
     * @param uid     用户id
     * @param pid     试卷号
     * @param ansmap  前端请求(答案列表)
     * @param ansmap
     * @return
     */
    @Override
    public Map<String, Object> markscore(int uid, int pid, int kid, Map ansmap) {
        long startTime=System.nanoTime();
        System.out.println("执行代码块/计算分数方法");
        //获取试卷信息
        Paper paper = findPaperbyid(pid);
        // 获取模板各个题型的题目分值
        float singleScore = paper.getSinscore();
        float mulScore = paper.getMulscore();
        float subfScore = paper.getSubscore();
        // 错题集
        List<String> wrongIds = new ArrayList<>();
        // 定义默认分值
        float score = 0;
        /* -------------------------- 开始评分 -------------------------- */
        // 加同步锁
        synchronized (this) {
            // 获取试问题列表
            List<Questions> questions = JSONObject.parseObject(paper.getQucontent(),List.class);
          //  List<Questions> questions = questionsService.getquestionbypid(pid);
            // 按照题目类型分组
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("count", questions.size());
            map.put("questionList", questions);
            String json = JSON.toJSONString(map, true);
            List<Questions> questionLists = JSON.parseArray(JSON.parseObject(json).getString("questionList"),Questions.class);
      //      List<Questions> questionl =questionList.stream().map(questions ->(QuestionsDTO) new QuestionsDTO().convertFrom(questions)).collect(Collectors.toList());

            Collection<List<Questions>> collection = questionLists.stream()
                    .sorted(Comparator.comparingInt(Questions::getType))
                    .collect(Collectors.groupingBy(Questions::getType)).values();
            for (List<Questions> questionList : collection) {
                switch (questionList.get(0).getType()) {
                    case (1):
                        // 单选题批改
                        AnsmarkDTO choiceMark = ScoreUtil.mark(questionList, singleScore, ansmap);
                        score += choiceMark.getScore();
                        wrongIds.addAll(choiceMark.getWrongIds());
                        break;
                    case (2):
                        // 多选题批改
                        AnsmarkDTO mulChoiceMark = ScoreUtil
                                .mulMark(questionList, mulScore,ansmap);
                        score += mulChoiceMark.getScore();
                        wrongIds.addAll(mulChoiceMark.getWrongIds());
                        break;
                    case (3):
                        // 主观题批改
                        AnsmarkDTO subMark = ScoreUtil.SubMark(questionList, subfScore,ansmap);
                        score += subMark.getScore();
                        wrongIds.addAll(subMark.getWrongIds());
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
        Map<String,Object> markinfo = new HashMap<>();
        markinfo.put("score",score);
        markinfo.put("wrong",wrong);

        // 封装分数参数，并将分数信息插入到分数表中
       // Examdata examdata = new Examdata(uid, pid,kid, String.valueOf(score), wrongStr);
        // 此处调用插入接口
    //    this.scoreService.save(scoreResult);
        long endTime=System.nanoTime();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ns");
        return markinfo;
    }

    @Override
    public List<String> stringToList(String strs) {
        return null;
    }
}