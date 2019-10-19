package com.wanxiu.repository;

import com.wanxiu.entity.LiveSysLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LiveSysLevelRepository  extends CrudRepository<LiveSysLevel,String> {

   Optional<LiveSysLevel> findById(String id);

   Iterable<LiveSysLevel> findAll();

   <S extends LiveSysLevel> S save(S s);

   @Query(nativeQuery = true,value = "select * from live_sys_level where min_ex<=?1 and max_ex>=?1")
   LiveSysLevel getLevelByEx(long ex);

   @Query(nativeQuery = true,value = "select max(max_ex) from live_sys_level")
   Integer getMaxEx();
}
