package com.q7w.examination.dto;

import com.q7w.examination.dto.base.OutputConverter;
import com.q7w.examination.entity.Paper;


/**
 * @author xiaogu
 * @date 2020/7/16 12:55
 **/
public class PaperDTO implements OutputConverter<PaperDTO, Paper> {
    private String name;//试卷名称
    private int type;//试卷类型1-自主命题 2-组卷 3-答题卡模式
    private float sinscore;//单选分数
    private float mulscore;//多选分数
    private float subscore;//主观分数
}
