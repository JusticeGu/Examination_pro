package com.q7w.examination.Service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.q7w.examination.Service.ExamDataService;
import com.q7w.examination.Service.ExroomService;
import com.q7w.examination.Service.RedisService;
import com.q7w.examination.Service.UserService;
import com.q7w.examination.dao.ExroomDAO;
import com.q7w.examination.entity.ExamSession;
import com.q7w.examination.entity.Exroom;
import com.q7w.examination.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import org.springframework.data.domain.Pageable;
import java.io.File;
import java.util.*;
/**
 * @author xiaogu
 * @date 2020/7/15 19:29
 **/
@Service
public class ExroomServiceimpl implements ExroomService {
    @Autowired
    UserService userService;
    @Autowired
    ExroomDAO exroomDAO;
    @Autowired
    ExamDataService examDataService;
    @Resource
    private RedisService redisService;
    /**
     * 考生进入考场
     *
     * @return 0-考场不存在 1-成功 2-不在时间范围 3-用户不在允许范围
     */
    @Override
    public Map enterExroom(int kid) {
        Map ansmap = new HashMap();
        String username = userService.getusernamebysu();
        if(username==null){
            ansmap.put("code","3");
            return ansmap;}//未登录或鸡贼登录拦截
        Exroom exroomInDB = findExroom(kid);
        String uno = userService.usernametouno(username);
        if((!checkpermission(String.valueOf(kid),uno))&&exroomInDB.getGrouptype()==1){  ansmap.put("code","3");
            return ansmap;}//不在考场接受范围拦截
        //     int ans = examDataService.addexamdata(kid,exroomInDB.getPid(),uno,exroomInDB.getAllowtimes());
        //      if (ans==-1||ans==2){  ansmap.put("code","4");
        //            return ansmap;}

        Date now= new Date();
        Long createtime = now.getTime();
        if (createtime>=exroomInDB.getDeadline()){ansmap.put("code","2");
            return ansmap;}//截止时间后进入拦截
        //第一位进入考场的考生向redis中写考场信息，用于后期提交校验时间
     //   if(!redisService.hasKey("exroom-"+kid)){
           //  long time = (exroomInDB.getStarttime()+exroomInDB.getTime()*60*1000-createtime)/1000;
    //        redisService.set("exroom-"+kid, "true",time);
      //  }
        //逻辑 添加考试记录 返回试题
      //  int kno = RandomUtil.toFixdLengthString(uno+kid+RandomUtil.generateDigitalString(3), 16);


        ansmap.put("code","3");
      //  ansmap.put("expiretime",redisService.getExpire("exroom-"+kid))
        return ansmap;
    }

    @Override
    public int startexrooom(int kid) {
        return 0;
    }

    @Override
    public int endroom(int kid) {
        return 0;
    }


    @Override
    public Page<Exroom> listexroombynum(Pageable pageable) {
        return exroomDAO.findAll(pageable);
    }

    @Override
    public List<String> stringToList(String strs){
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }
    @Override
    public Exroom findExroom(int kid) {

        return null;
    }

    @Override
    public boolean isExist(int kid) {
        return false;
    }

    @Override
    public List<Exroom> listExroom() {
        return exroomDAO.findAll();
    }

    @Override
    public int addExroom(Exroom exroom) {
        Date now= new Date();
        Long createtime = now.getTime();
        exroom.setCreateTime(createtime);
        exroom.setUpdateTime(createtime);
    //    exroom.setCreateBy(userService.getusernamebysu());
        exroom.setCreateBy("sys");
        try{
            exroomDAO.save(exroom);
            return 1;
        } catch (IllegalArgumentException e){
            return 2;
        }
    }

    @Override
    public int modifyExroom(Exroom exroom) {
        return 0;
    }

    @Override
    public int delExroom(int kid) {
        if(!isExist(kid)){return -1;}
        exroomDAO.deleteById(kid);
        return 0;
    }

    @Override
    public List<Map<String, Object>> getExroomList() {
        return null;
    }


    public int submitexam(ExamSession examSession) {
        return 0;
    }
    @Override
    public boolean checkpermission(String exid, String uno){
        //逻辑
        //   Set<Object> set = redisService.setMembers(exid);
        //   Set<String> set_old = new HashSet<String>();
        //  set_old.add(uno);
        //   boolean ans = set.contains(uno);
        boolean ans_2 =redisService.sHasKey(exid, uno);
        return ans_2;
    }

    /**
     * 考生导入
     * @param exid
     * @param uno
     * @return
     */
    @Override
    public boolean putpermission(String exid,String uno){
        //Set<String> set = new HashSet<String>();
        //  set.add(uno);
        //逻辑
        boolean ans = redisService.setadd(exid, uno);

        return ans;
    }

    @Override
    public int uploadgrouplist(MultipartFile multipartFile) {
        File file = FileUtil.toFile(multipartFile);
        // 读取 Excel 中的数据
        ExcelReader reader = ExcelUtil.getReader(file);
        reader.readAll();
        return 0;
    }
}
