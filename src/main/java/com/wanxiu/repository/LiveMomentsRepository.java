package com.wanxiu.repository;

import com.wanxiu.entity.LiveMoments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveMomentsRepository  extends CrudRepository<LiveMoments,String> {

   Optional<LiveMoments> findById(String id);

   Iterable<LiveMoments> findAll();

   <S extends LiveMoments> S save(S s);

   List<LiveMoments> findByCreateUserAndStatusOrderByCreateTimeDesc(Pageable pageAble, String createUser,int status);

}
