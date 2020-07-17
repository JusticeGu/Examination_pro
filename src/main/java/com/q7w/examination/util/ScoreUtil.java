package com.q7w.examination.util;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.q7w.examination.dto.AnsmarkDTO;
import com.q7w.examination.entity.Questions;
import org.apache.commons.text.similarity.JaccardSimilarity;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 试卷分数计算
 *
 * @author JusticeGu
 * @date 2020/7/8 14:02
 */
public class ScoreUtil {
    private ScoreUtil() {
    }
    public static void markscore(List<Questions> list, float score, Map map,int type){

    }


    /**
     * 基本评分方法
     *
     * @param list    问题集合
     * @param score   题目分值
     * @param map     用户提交的答案集合
     * @return 题型所得分值
     */
    public static AnsmarkDTO mark(List<Questions> list, float score, Map map) {
        float sum = 0;
        List<String> wrongIds = Lists.newArrayList();
        if (CollUtil.isNotEmpty(list)) {
            for (Questions q : list) {
                // 获取问题题目序号,提交的答案
              //  String res = request.getParameter(String.valueOf(q.getQid()));
                String res = map.get(String.valueOf(q.getQid())).toString();
                // 如果选项正确，加分
                if (q.getAnswer().equals(res)) {
                    sum += score;
                } else {
                    // 选择错误，加入错题集
                    wrongIds.add(String.valueOf(q.getQid()));
                }
            }
        }
        // 组装信息
        AnsmarkDTO info = new AnsmarkDTO();
        info.setScore(sum);
        info.setWrongIds(wrongIds);
        return info;
    }
    /**
     * 多选题批改
     *
     * @param list    问题集合
     * @param score   题目分值
     * @return 题型所得分值
     */
    public static AnsmarkDTO mulMark(List<Questions> list, float score, Map map) {
        float sum = 0;
        // 构建错题集集合
        List<String> wrongIds = Lists.newArrayList();
        // 判断问题集合不为空，空的话说明直接加入错题集
        if (CollUtil.isNotEmpty(list)) {
            // 循环正确答案
            out:for (Questions q : list) {
                // 从 request 对象中获取多选题参数
               // String[] res = request.getParameterValues(String.valueOf(q.getQid()));
                String resstr =  StrUtil.strip(map.get(String.valueOf(q.getQid())).toString(),"[","]");
               // String[] res = map.get(String.valueOf(q.getQid())).toString().split(",");
                List<String> res = StrUtil.split(resstr, StrUtil.C_COMMA);
                // 没有选答案就给零分
                if (res != null) {
                    List<String> rightKeys = StrUtil.split(StrUtil.strip(q.getAnswer(),"[","]"), StrUtil.C_COMMA);
                    if(isEquals(rightKeys,res)){
                        sum+=score;
                    } else {
                        for (String content : res) {
                            if (!rightKeys.contains(content)) {
                                wrongIds.add(String.valueOf(q.getQid()));
                                continue out;
                            }
                        }
                        sum+=score/2;
                    }
                } else {
                    wrongIds.add(String.valueOf(q.getQid()));
                }
            }
        }
        // 组装信息
        AnsmarkDTO info = new AnsmarkDTO();
        info.setScore(sum);
        info.setWrongIds(wrongIds);
        return info;
    }
    public static boolean isEquals(List<String> list1,List<String> list2){
        if(null != list1 && null != list2){
            if(list1.containsAll(list2) && list2.containsAll(list1)){
                return true;
            }
            return false;
        }
        return true;
    }
    /**
     * 主观题评分
     *
     * @param list    问题集合
     * @param score   题目分值
     * @param map     用户提交的答案集合
     * @return 题型所得分值
     */
    public static AnsmarkDTO SubMark(
            List<Questions> list, double score, Map map) {
        float sum = 0;
        // 错题集
        List<String> wrongIds = Lists.newArrayList();
        // 主观题/编程题答题记录
    //    List<StuAnswerRecord> stuAnswerRecords = Lists.newArrayList();
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        // 简答题批改
        if (CollUtil.isNotEmpty(list)) {
            float f = 0;
            for (Questions q : list) {
                // 不管主观题答题的质量如何，都放入错题集中
                wrongIds.add(String.valueOf(q.getQid()));
                // 从 request 对象中获取参数值
               // String res = request.getParameter(String.valueOf(q.getId()));
                String res = map.get(String.valueOf(q.getQid())).toString();
                // 获取正确答案
                String answer = q.getAnswer();
                // 计算 jaccard 相似系数
                double jcdSimilarity = jaccardSimilarity.apply(res, answer);
                // 计算基于相似系数计算的基础分数（1. 相当于计算关键词的得分）
                double calScore = jcdSimilarity * score;
                // 字数不足且分数大于1，扣1分（2. 相当于计算字数）
                if (res.length() < answer.length()/1.1) {
                    calScore = calScore >= 2.50d ? calScore * 0.70 : calScore * 0.75;
                }
                f += calScore;
                // 封装主观题答题记录参数

            }
            // 格式化分数的类型为 int 类型
       //     String q = df.format(f);
            sum = (float)(Math.round(f*100))/100;
        }
        // 封装参数
        AnsmarkDTO info = new AnsmarkDTO();
        // 得分
        info.setScore(sum);
        // 错题
        info.setWrongIds(wrongIds);
        // 答题记录
        return info;
    }

}
