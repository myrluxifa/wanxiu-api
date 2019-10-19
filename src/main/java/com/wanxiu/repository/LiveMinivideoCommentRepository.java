package com.wanxiu.repository;

import com.wanxiu.entity.LiveMinivideoComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveMinivideoCommentRepository  extends CrudRepository<LiveMinivideoComment,String> {

   Optional<LiveMinivideoComment> findById(String id);

   Iterable<LiveMinivideoComment> findAll();

   <S extends LiveMinivideoComment> S save(S s);

    List<LiveMinivideoComment> findByVideoIdOrderByCreateTimeDesc(Pageable pagePlugin, String videoId);
}
