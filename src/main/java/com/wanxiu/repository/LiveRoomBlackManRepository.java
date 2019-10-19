package com.wanxiu.repository;

import com.wanxiu.entity.LiveRoomBlackMan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveRoomBlackManRepository  extends CrudRepository<LiveRoomBlackMan,String> {

   Optional<LiveRoomBlackMan> findById(String id);

   Iterable<LiveRoomBlackMan> findAll();

   <S extends LiveRoomBlackMan> S save(S s);

    LiveRoomBlackMan findByUserIdAndShowId(String userId, Integer showId);
}
