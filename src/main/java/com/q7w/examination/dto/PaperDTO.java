package com.q7w.examination.dto;

import com.q7w.examination.dto.base.OutputConverter;
import com.q7w.examination.entity.Paper;
import lombok.Data;
import lombok.ToString;


/**
 * @author xiaogu
 * @date 2020/7/16 12:55
 **/
@Data
@ToString
public class PaperDTO implements OutputConverter<PaperDTO,Paper> {
    private int pid;//试卷号
    private String name;//试卷名称
    private int type;//试卷类型1-自主命题 2-组卷 3-答题卡模式
    private int status;
    private boolean enable;

}
