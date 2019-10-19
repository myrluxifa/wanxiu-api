package com.wanxiu.repository;

import com.wanxiu.entity.LiveSlitMoneyLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveSlitMoneyLogRepository  extends CrudRepository<LiveSlitMoneyLog,String> {

   Optional<LiveSlitMoneyLog> findById(String id);

   Iterable<LiveSlitMoneyLog> findAll();

   <S extends LiveSlitMoneyLog> S save(S s);

}
