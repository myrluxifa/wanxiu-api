package com.wanxiu.repository;

import com.wanxiu.entity.LiveOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveOrderRepository  extends CrudRepository<LiveOrder,String> {

   Optional<LiveOrder> findById(String id);

   Iterable<LiveOrder> findAll();

   <S extends LiveOrder> S save(S s);

}
