package com.wanxiu.repository;

import com.wanxiu.entity.LiveGift;
import com.wanxiu.entity.LiveSysGuardian;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveSysGuardianRepository  extends CrudRepository<LiveSysGuardian,String> {

   Optional<LiveSysGuardian> findById(String id);

   Iterable<LiveSysGuardian> findAll();

   <S extends LiveSysGuardian> S save(S s);

   List<LiveSysGuardian> findByStatus(int status);

}
