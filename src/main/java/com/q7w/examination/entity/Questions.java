package com.q7w.examination.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Questions extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    /**
     * 难度
     */
    private static final Integer EASY = 1;
    private static final Integer MEDIUM = 2;
    private static final Integer HARD = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 答案
     */
    private String answer;
    /**
     * 解析
     */
    private String context;
    /**
     * 题目类型 1-单选 2-多选 3-填空 4-主观
     */
    private int type;
    /**
     * 所属课程
     */
    private int cid;
    /**
     * 难度
     */
    private  Integer diffcult;
//备注
    private String remarks;


}
