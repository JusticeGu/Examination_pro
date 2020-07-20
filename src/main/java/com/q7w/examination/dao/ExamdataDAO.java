package com.q7w.examination.dao;

import com.q7w.examination.entity.Examdata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xiaogu
 * @date 2020/7/17 17:21
 **/
public interface ExamdataDAO extends JpaRepository<Examdata,Integer> {
    List<Examdata> findAllByUno(String uno);
    List<Examdata> findAllByKid(int kid);
    int countAllByKid(int kid);
    Examdata findByKidAndPidAndUno(int kid,int pid,String uno);

}
