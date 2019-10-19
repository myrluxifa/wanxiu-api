package com.wanxiu.repository;

import com.wanxiu.entity.LiveHongbao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveHongbaoRepository  extends CrudRepository<LiveHongbao,String> {

   Optional<LiveHongbao> findById(String id);

   List<LiveHongbao> findByShowIdAndStatus(String showId, int status);

   Iterable<LiveHongbao> findAll();

   <S extends LiveHongbao> S save(S s);

}
