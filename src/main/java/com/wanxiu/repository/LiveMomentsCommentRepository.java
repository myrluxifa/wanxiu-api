package com.wanxiu.repository;

import com.wanxiu.entity.LiveMomentsComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveMomentsCommentRepository  extends CrudRepository<LiveMomentsComment,String> {

   Optional<LiveMomentsComment> findById(String id);

   Iterable<LiveMomentsComment> findAll();

   <S extends LiveMomentsComment> S save(S s);

   List<LiveMomentsComment> findByMomentId(String momentId, Pageable pageAble);

   int countByMomentId(String momentId);

    List<LiveMomentsComment> findByMomentIdOrderByCreateTimeDesc(String id, Pageable pagePlugin);
}
