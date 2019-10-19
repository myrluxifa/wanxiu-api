package com.wanxiu.repository;

import com.wanxiu.entity.LiveMinivideoLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveMinivideoLikeRepository  extends CrudRepository<LiveMinivideoLike,String> {

   Optional<LiveMinivideoLike> findById(String id);

   Iterable<LiveMinivideoLike> findAll();

   <S extends LiveMinivideoLike> S save(S s);

    int deleteByVideoIdAndUserId(String videoId, String userId);
}
