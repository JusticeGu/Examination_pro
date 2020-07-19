package com.q7w.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "examSession")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class ExamSession extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3033545151355633270L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//考场号
    private int kno;//考试号
    private long starttime;//进去时间
    private long deadline;//截止时间（非提交时间）
    private String objsinqunlist;//单选问题列表
    private String objsinanslist;//单选用户答案列表
    private String objmulqunlist;//多选问题列表
    private String objmulanslist;//多选用户答案列表
    private String subqunlist;//主观问题列表
    private String subanslist;//主观用户答案列表

    private int status;
}

