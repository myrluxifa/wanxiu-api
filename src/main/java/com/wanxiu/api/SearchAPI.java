package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveHotSearch;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.repository.LiveFansRepository;
import com.wanxiu.repository.LiveHotSearchRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags={"搜索"})
@RestController
@RequestMapping("api/search")
public class SearchAPI {

    @Autowired
    private LiveUserRepository userRepository;

    @Autowired
    private LiveFansRepository fansRepository;

    @Autowired
    private LiveHotSearchRepository liveHotSearchRepository;


    @PostMapping("hotwords")
    @ApiOperation(value = "热门搜索词")
    public ResEntity<Object> keywords() {

        return new ResEntity<>(Common.RESULT.SUCCESS, liveHotSearchRepository.findByStatusOrderByIsAdminDescCountDesc(0));
    }

    @PostMapping("page")
    @ApiOperation(value = "搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "userId",value="userId",required = true),
            @ApiImplicitParam(paramType = "query",name = "keywords",value="关键字",required = true),
            @ApiImplicitParam(paramType = "query",name = "page",value="1",required = true),
            @ApiImplicitParam(paramType = "query",name = "size",value="20",required = true)
    })
    public ResEntity<Object> search(Integer page, Integer size, String userId, String keywords) {

        List<LiveUser> users = userRepository.findByShowIdIsNotNullAndNicknameLikeOrSignatureLikeOrderByLastShowTimeDesc(PagePlugin.pagePlugin(page, size), "%"+keywords+"%", "%"+keywords+"%");

        users.stream().forEach(u -> {
            if(fansRepository.findByFansAndIdol(userId, u.getId())
                    .isPresent()) {
                u.setIsfans(true);
            } else {
                u.setIsfans(false);
            }
        });

        Optional<LiveHotSearch> lhs = liveHotSearchRepository.findByContentAndStatus(keywords, 0);
        if (lhs.isPresent()) {
            lhs.get().setCount(lhs.get().getCount() + 1);
            liveHotSearchRepository.saveAndFlush(lhs.get());
        } else {
            LiveHotSearch hs = new LiveHotSearch();
            hs.setStatus(0);
            hs.setCount(1);
            hs.setContent(keywords);
            hs.setIsAdmin(0);
            liveHotSearchRepository.saveAndFlush(hs);
        }

        return new ResEntity<>(Common.RESULT.SUCCESS, users);
    }
}
