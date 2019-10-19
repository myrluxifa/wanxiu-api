package com.wanxiu.repository;

import com.wanxiu.entity.LiveSysSlitRatio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveSysSlitRatioRepository  extends CrudRepository<LiveSysSlitRatio,String> {

   Optional<LiveSysSlitRatio> findById(String id);

   Iterable<LiveSysSlitRatio> findAll();

   <S extends LiveSysSlitRatio> S save(S s);

   Optional<LiveSysSlitRatio> findByType(String type);

}
