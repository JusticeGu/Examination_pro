package com.q7w.examination.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

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
    private int singlenum;//单选数量
    private int mulnum;//多选数量
    private int multype;//多选题得分逻辑 1-全对得分 2-错选扣分
    private int subnum;//主观数量
    private float sinscore;//单选分数
    private float mulscore;//多选分数
    private float subscore;//主观分数
    private String scorelist;//分数列表---自主命题试卷可用
    private String sinans;//单选答案列表
    private String mulans;//多选答案列表

}
