package com.wanxiu.repository;

import com.wanxiu.entity.LiveSysAppVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveSysAppVersionRepository  extends CrudRepository<LiveSysAppVersion,String> {

   Optional<LiveSysAppVersion> findById(String id);

   Iterable<LiveSysAppVersion> findAll();

   <S extends LiveSysAppVersion> S save(S s);

   @Query(nativeQuery = true,value = "select * from live_sys_app_version where type=?1 and version_num>?2 order by version_num desc")
   List<LiveSysAppVersion> findByTypeAndVersion(int type, String versionNum);

}
