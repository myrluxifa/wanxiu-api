package com.wanxiu.repository;

import com.wanxiu.entity.LiveHongbaoLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveHongbaoLogRepository  extends CrudRepository<LiveHongbaoLog,String> {

   Optional<LiveHongbaoLog> findById(String id);

   Iterable<LiveHongbaoLog> findAll();

   <S extends LiveHongbaoLog> S save(S s);

   @Query(value = "select sum(amount) from live_hongbao_log where hongbao_id=?1",nativeQuery = true)
   int sumHongBao(String hongBaoKey);

   List<LiveHongbaoLog> findByHongbaoId(String hongbaoId);

   @Query(value = "select (select nickname from live_user where id=l.user_id),amount from live_hongbao_log l where hongbao_id=?1 ",nativeQuery = true)
   List<Object[]> queryByHongbaoId(String hongbaoId);
}
