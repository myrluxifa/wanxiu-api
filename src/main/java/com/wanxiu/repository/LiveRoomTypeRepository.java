package com.wanxiu.repository;

import com.wanxiu.entity.LiveRoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveRoomTypeRepository  extends CrudRepository<LiveRoomType,String> {

   Optional<LiveRoomType> findById(String id);

   Iterable<LiveRoomType> findAll();

   <S extends LiveRoomType> S save(S s);

   List<LiveRoomType> findByStatusOrderByOrder(int status);

}
