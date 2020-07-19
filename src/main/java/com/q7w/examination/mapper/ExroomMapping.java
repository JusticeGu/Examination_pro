package com.q7w.examination.mapper;


import com.q7w.examination.entity.Exroom;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExroomMapping {
    @Select("Select * FROM exroom WHERE name = #{name}")
    Exroom queryByName(@Param("name") String name);

    @Select("Select * FROM exroom WHERE kid = #{kid}")
    Exroom queryBykid(@Param("kid") int kid);

    @Select("SELECT * FROOM  user")
    List<Exroom> queryall();

    @Insert("INSERT INTO exroom(eid,name,remark,enable,group,starttime,deadline,time,type) " +
            "VALUES(#{eid},#{name},#{remark},#{enable},#{group},#{starttime},#{deadline},#{time},#{type})")
    int add(Exroom exroom);

    @Delete("DELETE FROM exroom WHERE kid = #{kid}")
    int deleByid(int kid);
    @Update("UPDATE exroom SET eid=#{eid},#{name},remark=#{remark}," +
            "enable=#{enable},group=#{group},starttime=#{starttime},deadline=#{deadline},time=#{time},tyoe=#{type})" +
            "WHERE kid = #{kid}")
    int updatebyid(Exroom exroom);
}
