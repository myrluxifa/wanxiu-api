package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveGiftRank;
import com.wanxiu.repository.LiveGiftRankRepository;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Api(value = "RankAPI", tags = {"排行榜"})
@RestController
@RequestMapping("api")
public class RankAPI {

    @Autowired
    private LiveGiftRankRepository rankRepository;

    @ApiOperation(value = "礼物排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "1", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "20", required = true),
            @ApiImplicitParam(paramType = "query", name = "showId", value = "10086"),
            @ApiImplicitParam(paramType = "query", name = "userType", value = "G-守护 N-普通用户 R-富豪榜 S-明星榜", required = true),
            @ApiImplicitParam(paramType = "query", name = "type", value = "D-日 W-周 M-月 ALL-超级榜 SALL-主播超级榜", required = true)
    })
    @PostMapping("gift/rankNew")
    public ResEntity<Object> giftRankNew(Integer page, Integer size, Integer showId, String userType, String type) {

        Page<LiveGiftRank> p = rankRepository.findAll(new Specification<LiveGiftRank>() {
            @Override
            public Predicate toPredicate(Root<LiveGiftRank> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (!type.isEmpty()) {
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("cycleId"), getCycleId(type)));
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("type"), type));
                }

                if (!userType.isEmpty()) {
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("userType"), userType));
                }

                predicate.getExpressions().add(criteriaBuilder.equal(root.get("showId"), showId));

                return predicate;
            }
        }, PagePlugin.pagePlugin(page, size));

        return new ResEntity(Common.RESULT.SUCCESS, p.getTotalElements() + "", p.getContent());
    }

    @ApiOperation(value = "礼物排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "1", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "20", required = true),
            @ApiImplicitParam(paramType = "query", name = "showId", value = "10086"),
            @ApiImplicitParam(paramType = "query", name = "userType", value = "G-守护 N-普通用户 R-富豪榜 S-明星榜", required = true),
            @ApiImplicitParam(paramType = "query", name = "type", value = "D-日 W-周 M-月 ALL-超级榜 SALL-主播超级榜", required = true)
    })
    @PostMapping("gift/rank")
    public ResEntity<Object> giftRank(Integer page, Integer size, Integer showId, String userType, String type) {


        return new ResEntity<>(Common.RESULT.SUCCESS, rankRepository.countByShowIdAndUserTypeAndTypeAndCycleId(showId, userType, type, getCycleId(type)).toString(),
                rankRepository.findByShowIdAndUserTypeAndTypeAndCycleId(
                PagePlugin.pagePluginSort(page, size, Sort.Direction.DESC, "coins"),
                showId, userType, type, getCycleId(type)));
    }
//    public static List<String> getUserType(String type) {
//        List<String> arr = new ArrayList<>();
//        switch (type) {
//            case "G": arr.add("G"); break;
//            case "N": arr.add("N"); break;
//            case "R": arr.add("G"); arr.add("N"); break;
//            case "S": arr.add("S"); break;
//        }
//
//        return arr;
//    }

    public static String getCycleId(String type) {
        String pattern = "";
        switch (type) {
            case "D":
                pattern = "yyyyMMdd";
                break;
            case "W":
                pattern = "ww";
                break;
            case "M":
                pattern = "yyyyMM";
                break;
            case "ALL":
                return "ALL";
            default:
                ;
        }

        return DateFormatUtils.format(Calendar.getInstance().getTime(), pattern);
    }
}
