package com.wanxiu.repository;

import com.wanxiu.entity.LiveSysRatio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveSysRatioRepository  extends CrudRepository<LiveSysRatio,String> {

   Optional<LiveSysRatio> findById(String id);

   Iterable<LiveSysRatio> findAll();

   <S extends LiveSysRatio> S save(S s);

}
