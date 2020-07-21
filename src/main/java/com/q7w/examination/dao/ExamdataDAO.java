package com.q7w.examination.dao;

import com.q7w.examination.entity.Examdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xiaogu
 * @date 2020/7/17 17:21
 **/
public interface ExamdataDAO extends JpaRepository<Examdata,Integer> {
    List<Examdata> findAllByUno(String uno);
    List<Examdata> findAllByKid(int kid);
    Examdata findByKidAndPidAndUno(int kid,int pid,String uno);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1")
    Integer countByKid(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and totalscore >= 60")
    Integer countPass(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and totalscore >= 80")
    Integer countExcellent(int kid);
    @Query(nativeQuery =true,value = "select sum(totalscore) from examdata where kid = ?1")
    Integer sumOfScore(int kid);
    @Query(nativeQuery =true,value = "select max(totalscore) from examdata where kid = ?1")
    Integer maxOfScore(int kid);
    @Query(nativeQuery =true,value = "select min(totalscore) from examdata where kid = ?1")
    Integer minOfScore(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and totalscore >= 60 and totalscore < 80")
    Integer countGood(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and totalscore >= 40 and totalscore < 60")
    Integer countBad(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and totalscore < 40")
    Integer countPoor(int kid);
    @Query(nativeQuery =true,value = "select pid from examdata where kid = ?1 limit 1")
    int getPid(int kid);
    @Query(nativeQuery =true,value = "select count(*) from examdata where kid = ?1 and wronglist like %?2%")
    Integer getNumOfWrong(int kid, int i);
}
