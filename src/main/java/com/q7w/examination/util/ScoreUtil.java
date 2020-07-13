package com.q7w.examination.util;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.q7w.examination.dto.AnsmarkDTO;
import com.q7w.examination.entity.Questions;
import org.apache.commons.text.similarity.JaccardSimilarity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    /**
     * 基本评分方法
     *
     * @param list    问题集合
     * @param score   题目分值
     * @param request request 对象
     * @return 题型所得分值
     */
    public static AnsmarkDTO mark(List<Questions> list, float score, HttpServletRequest request) {
        float sum = 0;
        List<String> wrongIds = Lists.newArrayList();
        if (CollUtil.isNotEmpty(list)) {
            for (Questions q : list) {
                // 获取问题题目序号
                String res = request.getParameter(String.valueOf(q.getQid()));
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
     * @param request request 对象
     * @return 题型所得分值
     */
    public static AnsmarkDTO mulMark(List<Questions> list, float score, HttpServletRequest request) {
        float sum = 0;
        // 构建错题集集合
        List<String> wrongIds = Lists.newArrayList();
        // 判断问题集合不为空，空的话说明直接加入错题集
        if (CollUtil.isNotEmpty(list)) {
            // 循环正确答案
            out:for (Questions q : list) {
                // 从 request 对象中获取多选题参数
                String[] res = request.getParameterValues(String.valueOf(q.getQid()));
                // 没有选答案就给零分
                if (res != null) {
                    List<String> rightKeys = StrUtil.split(q.getAnswer(), StrUtil.C_COMMA);
                    if(q.getAnswer().equals(res)){
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

}
