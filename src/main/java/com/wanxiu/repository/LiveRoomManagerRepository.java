package com.wanxiu.repository;

import com.wanxiu.entity.LiveRoomManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveRoomManagerRepository  extends CrudRepository<LiveRoomManager,String> {

   Optional<LiveRoomManager> findById(String id);

   Iterable<LiveRoomManager> findAll();

   <S extends LiveRoomManager> S save(S s);

    List<LiveRoomManager> findByShowIdOrderByCreateTimeDesc(Integer showId);

    LiveRoomManager findByShowIdAndUserId(Integer showId, String userId);
}
