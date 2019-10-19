package com.wanxiu.repository;

import com.wanxiu.entity.LiveGift;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveGiftRepository  extends CrudRepository<LiveGift,String> {

   Optional<LiveGift> findById(String id);

   Iterable<LiveGift> findAll();

   <S extends LiveGift> S save(S s);

   List<LiveGift> findByStatus(int status);

   List<LiveGift> findByStatusAndGiftType(int status,int type);

}
