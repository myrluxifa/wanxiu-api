package com.wanxiu.repository;

import com.wanxiu.entity.LiveFans;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveFansRepository  extends CrudRepository<LiveFans,String> {

   Optional<LiveFans> findById(String id);

   Iterable<LiveFans> findAll();

   <S extends LiveFans> S save(S s);

   Optional<LiveFans> findByFansAndIdol(String fans,String idol);

   @Query(value = "select f.*,u.nickname,u.sex,u.signature,u.experience,u.head_portrait headPortrait from live_fans f left join live_user u on f.idol=u.id where f.fans=?3 limit ?1,?2 ",nativeQuery = true)
   List<Object[]> findByFans(int page, int size, String fans);

    @Query(value = "select f.*,u.nickname,u.sex,u.signature,u.experience,u.head_portrait headPortrait from live_fans f left join live_user u on f.fans=u.id where f.idol=?3 limit ?1,?2 ",nativeQuery = true)
    List<Object[]> findByIdol(int page, int size, String fans);

    List<LiveFans> findByIdol(Pageable create_time, String userId);

    int countByIdol(String idol);

    int countByFans(String fans);
}
