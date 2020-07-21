package com.q7w.examination.controller;


import com.q7w.examination.Service.DataVisualizationService;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JunXxxxi
 * @date 2020/7/20 18:10
 **/

@RestController
@Api(tags = "数据可视化接口")
@RequestMapping("/api/datavisualization")
public class DataVisualizationController {
    @Autowired
    DataVisualizationService dataVisualizationService;

    @GetMapping("/numOfExam")
    @ApiOperation("老师端近7天发布考试数量情况统计")
    public ResponseData getNumOfExam(){
        return new ResponseData(ExceptionMsg.SUCCESS, dataVisualizationService.getNumOfExam());
    }

    @GetMapping("/generalDataOfTeacher")
    @ApiOperation("老师端单次考试数据概况")
    public ResponseData getNumOfStudents(@RequestParam int kid){
        return new ResponseData(ExceptionMsg.SUCCESS, dataVisualizationService.getNumOfStudents(kid));
    }

    @GetMapping("/disOfScore")
    @ApiOperation("老师端单次考试分数分布")
    public ResponseData getDisOfScore(@RequestParam int kid){
        return new ResponseData(ExceptionMsg.SUCCESS, dataVisualizationService.getDisOfScore(kid));
    }

    @GetMapping("/wrongSituation")
    @ApiOperation("错误最多的题目统计")
    public ResponseData getWrongSituation(@RequestParam int kid){
        return new ResponseData(ExceptionMsg.SUCCESS, dataVisualizationService.getWrongSituation(kid));
    }

    @GetMapping("/riaghtRate")
    @ApiOperation("老师端题目正确率统计")
    public ResponseData getRiaghtRate(@RequestParam int kid){
        return new ResponseData(ExceptionMsg.SUCCESS, dataVisualizationService.getRiaghtRate(kid));
    }


}
