package com.wanxiu.repository;

import com.wanxiu.entity.LiveViewRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveViewRecordRepository  extends CrudRepository<LiveViewRecord,String> {

   Optional<LiveViewRecord> findById(String id);

   Iterable<LiveViewRecord> findAll();

   <S extends LiveViewRecord> S save(S s);

    List<LiveViewRecord> findByCreateUserOrderByCreateTimeDesc(Pageable pagePlugin, String userId);

    LiveViewRecord findByCreateUserAndShowId(String userID, Integer showId);

    void deleteByCreateUser(String userId);
}
