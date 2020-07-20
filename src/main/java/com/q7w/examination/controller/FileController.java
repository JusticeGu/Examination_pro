package com.q7w.examination.controller;

import com.q7w.examination.Service.FileService;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @ResponseBody
    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public ResponseData ossuoload(MultipartFile file){
        //判断图片是否为空
        if (file.isEmpty()) {
            return new ResponseData(ExceptionMsg.FAILED_F,"文件数据为null");
        }
        String ans = fileService.fileupload(file);
        switch (ans) {
            case "filenull":
                return new ResponseData(ExceptionMsg.FAILED_F,"文件数据为null");
            case "imguploadfalse":
                return new ResponseData(ExceptionMsg.FAILED,"上传发生错误请稍后再试");
        }
        return new ResponseData(ExceptionMsg.SUCCESS,ans);
    }
}
