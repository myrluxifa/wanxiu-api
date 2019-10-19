package com.wanxiu.repository;

import com.wanxiu.entity.LiveCoinsLog;
import com.wanxiu.util.PagePlugin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveCoinsLogRepository  extends CrudRepository<LiveCoinsLog,String> {

   Optional<LiveCoinsLog> findById(String id);

   Iterable<LiveCoinsLog> findAll();

   <S extends LiveCoinsLog> S save(S s);

   List<LiveCoinsLog> findByUserIdAndTypeIn(Pageable createTime, String userId, List<String> types);

    List<LiveCoinsLog> findByUserId(Pageable createTime, String userId);

    List<LiveCoinsLog> findByUserIdOrderByCreateTimeDesc(String userId, Pageable pageable);

    @Query(nativeQuery = true,value = "select IFNULL(sum(coins),0) from live_coins_log where type=?1 and user_id=?2")
    int sumByTypeAndUserId(String type,String userId);
}
