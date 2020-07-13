package com.q7w.examination.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "paper")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Paper extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;//试卷号
    private int type;//试卷类型1-自主命题 2-组卷 3-答题卡模式
    private int status;
    private boolean enable;
    private String questionId;//问题号
    private float sinscore;//单选分数
    private float mulscore;//多选分数
    private float subscore;//主观分数
    private int singlenum;//单选数量
    private int mulnum;//多选数量
    private int subnum;//主观数量
    @Transient
    private List<Map<String,Object>> paperQuestions;//前端显示
    @Transient
    private List<Questions> questionsList;//问题列表后端映射


}
