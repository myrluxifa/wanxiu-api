package com.wanxiu.repository;

import com.wanxiu.entity.WxPayNotifyLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WxPayNotifyLogRepository  extends CrudRepository<WxPayNotifyLog,String> {

   Optional<WxPayNotifyLog> findById(String id);

   Iterable<WxPayNotifyLog> findAll();

   <S extends WxPayNotifyLog> S save(S s);

}
