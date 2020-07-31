package com.q7w.examination.dto;

import com.q7w.examination.dto.base.OutputConverter;
import com.q7w.examination.entity.QuestionCategory;
import com.q7w.examination.entity.Questions;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class QuestionsDTO implements OutputConverter<QuestionsDTO,Questions> {

    private int qid;
    /**
     * 题干
     */
    private String questionName;
    /**
     * 题目图片
     */
    private String questionImg;
    /**
     * 题目选项组
     */
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String optionF;


    /**
     * 题目类型 1-单选 2-多选 3-填空 4-主观
     */
    private int type;
    /**
     * 所属课程
     */
    private int cid;
}
