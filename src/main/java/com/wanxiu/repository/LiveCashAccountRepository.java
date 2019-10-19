package com.wanxiu.repository;

import com.wanxiu.entity.LiveCashAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveCashAccountRepository  extends CrudRepository<LiveCashAccount,String> {

   Optional<LiveCashAccount> findById(String id);

   Optional<LiveCashAccount> findByUserId(String userId);

   Iterable<LiveCashAccount> findAll();

   <S extends LiveCashAccount> S save(S s);

}
