package com.wanxiu.repository;

import com.wanxiu.entity.LiveCashWithdrawal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveCashWithdrawalRepository  extends CrudRepository<LiveCashWithdrawal,String> {

   Optional<LiveCashWithdrawal> findById(String id);

   Iterable<LiveCashWithdrawal> findAll();

   <S extends LiveCashWithdrawal> S save(S s);

   List<LiveCashWithdrawal> findByUserId(String userId);

   int countByUserIdAndStatus(String userId,int status);

   List<LiveCashWithdrawal> findByUserIdOrderByCreateTimeDesc(String userId, Pageable pageable);
}
