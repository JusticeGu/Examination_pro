package com.q7w.examination.controller;

import com.q7w.examination.Service.FileService;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author xiaogu
 * @date 2020/7/18 12:55
 **/
@RestController
@Api(tags = "文件服务接口")
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    FileService fileService;
    @GetMapping("/uploadfile")
    @ApiOperation("OSS文件上传接口")
    public ResponseData ossuoload(MultipartFile multipartFile){
        //判断图片是否为空
        if (multipartFile.isEmpty()) {
            return new ResponseData(ExceptionMsg.FAILED_F,"文件数据为null");
        }
        String ans = fileService.fileupload(multipartFile);
        switch (ans) {
            case "filenull":
                return new ResponseData(ExceptionMsg.FAILED_F,"文件数据为null");
            case "imguploadfalse":
                return new ResponseData(ExceptionMsg.FAILED,"上传发生错误请稍后再试");
        }
        return new ResponseData(ExceptionMsg.SUCCESS,ans);
    }
}
