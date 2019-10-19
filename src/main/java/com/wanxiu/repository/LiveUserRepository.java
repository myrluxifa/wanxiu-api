package com.wanxiu.repository;

import com.wanxiu.entity.LiveUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveUserRepository  extends CrudRepository<LiveUser,String> {

   Optional<LiveUser> findById(String id);

   Iterable<LiveUser> findAll();

   <S extends LiveUser> S save(S s);

   Optional<LiveUser> findByUserName(String phone);

   int countByUserName(String phone);

   int countByOpenId(String openId);

   Optional<LiveUser> findByOpenId(String openId);

    LiveUser findByShowId(Integer showId);

    List<LiveUser> findByShowIdIsNotNullAndNicknameLikeOrSignatureLikeOrderByLastShowTimeDesc(Pageable pagePlugin, String keywords, String keywords1);
}
