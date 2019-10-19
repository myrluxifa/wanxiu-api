package com.wanxiu.repository;

import com.wanxiu.entity.AliPayNotifyLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AliPayNotifyLogRepository  extends CrudRepository<AliPayNotifyLog,String> {

   Optional<AliPayNotifyLog> findById(String id);

   Iterable<AliPayNotifyLog> findAll();

   <S extends AliPayNotifyLog> S save(S s);

}
