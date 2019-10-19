package com.wanxiu.repository;

import com.wanxiu.entity.LiveMomentsLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveMomentsLikeRepository  extends CrudRepository<LiveMomentsLike,String> {

   Optional<LiveMomentsLike> findById(String id);

   Iterable<LiveMomentsLike> findAll();

   <S extends LiveMomentsLike> S save(S s);

   int countByMomentsId(String momentsId);

   int countByMomentsIdAndUserId(String momentsId,String userId);

}
