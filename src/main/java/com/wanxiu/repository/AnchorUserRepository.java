package com.wanxiu.repository;

import com.wanxiu.entity.LiveUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnchorUserRepository extends CrudRepository<LiveUser,String> {
    Optional<LiveUser> findById(String id);

    Iterable<LiveUser> findAll();

    <S extends LiveUser> S save(S s);

    Optional<LiveUser> findByUserName(String phone);
}
