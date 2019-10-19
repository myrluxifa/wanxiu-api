package com.wanxiu.repository;

import com.wanxiu.entity.LiveMinivideo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveMinivideoRepository  extends CrudRepository<LiveMinivideo,String> {

   Optional<LiveMinivideo> findById(String id);

   Iterable<LiveMinivideo> findAll();

   <S extends LiveMinivideo> S save(S s);

    List<LiveMinivideo> findByCreateUserOrderByCreateTimeDesc(Pageable pagePlugin, String userId);

    @Query(nativeQuery = true,value = "select a.id, a.title, a.poster_url, a.url, a.praise, a.comment, a.share, a.views, a.status, a.create_time, a.create_user, " +
            "(select count(1) from live_minivideo_like b where a.id = b.video_id and b.user_id = :userId) remark from live_minivideo a limit :page, :pageSize")
    List<LiveMinivideo> page(@Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("userId") String userId);
}
