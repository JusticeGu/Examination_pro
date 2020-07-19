package com.q7w.examination.Service.impl;

import com.q7w.examination.Service.FileService;
import com.q7w.examination.util.OSSClientUtils;
import com.q7w.examination.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiaogu
 * @date 2020/7/18 12:59
 **/
@Service
public class FileServiceimpl implements FileService {
    @Override
    public String fileupload(MultipartFile multipartFile) {
        //判断图片是否为空
        if (multipartFile.isEmpty()) {
            return "filenull";
        }
        //上传到图片服务器
        try {
            //获取图片扩展名
            String originalFilename = multipartFile.getOriginalFilename();
            String extName = multipartFile.getOriginalFilename().substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = RandomUtil.generateString(12) + "." + extName;
            String url = OSSClientUtils.uploadFile(multipartFile.getBytes(), "/UserFile/" + fileName);
            url = OSSClientUtils.ACCESS_BASE_PATH + "/" + url;
            System.out.println("图片地址:" + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return "imguploadfalse";
        }
    }
}
