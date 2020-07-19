package com.q7w.examination.controller;

import com.q7w.examination.Service.AnnouncementService;
import com.q7w.examination.entity.Announcement;
import com.q7w.examination.result.ExceptionMsg;
import com.q7w.examination.result.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author JunXxxxi
 * @date 2020/7/19 11:10
 **/
@RestController
@Api(tags = "公告接口")
@RequestMapping("/api/announcement")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;
    @GetMapping("/alist")
    @ApiOperation("公告列表")
    public ResponseData getAnnouncementList(){
        return new ResponseData(ExceptionMsg.SUCCESS, announcementService.announcementList());
    }

    @PostMapping("/adda")
    @ApiOperation("添加公告")
    public ResponseData addAnnouncement(@RequestBody Announcement announcement){
        int ans = announcementService.addAnnouncement(announcement);
        switch (ans){
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"公告添加成功，请勿重复操作");
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"添加失败");
            case 3:
                return new ResponseData(ExceptionMsg.FAILED_F,"参数非法，请从正规接口操作或联系管理员");
        }
        return new ResponseData(ExceptionMsg.FAILED,"后端错误");
    }

    @DeleteMapping("/dela")
    @CrossOrigin
    @ApiOperation("删除公告")
    public ResponseData delAnnouncement(@RequestParam int aid){
        int status = announcementService.delAnnouncement(aid);
        switch (status) {
            case -1:
                return new ResponseData(ExceptionMsg.FAILED,"公告不存在或已删除请勿重复操作");
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"删除成功");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }

    @PostMapping("/modia")
    @CrossOrigin
    @ApiOperation("修改公告")
    public ResponseData modifyAnnouncement(@RequestBody Announcement announcement){
        int status = announcementService.modifyAnnouncement(announcement);
        switch (status) {
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS,"修改成功" );
            case 2:
                return new ResponseData(ExceptionMsg.FAILED,"修改失败");
        }
        return new ResponseData(ExceptionMsg.FAILED_F,"后端错误");
    }



}
