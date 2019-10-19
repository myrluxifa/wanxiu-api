package com.wanxiu.repository;

import com.wanxiu.entity.LiveAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveAccountRepository  extends CrudRepository<LiveAccount,String> {

   Optional<LiveAccount> findById(String id);

   Iterable<LiveAccount> findAll();

   <S extends LiveAccount> S save(S s);

   Optional<LiveAccount> findByUserId(String userId);

}
