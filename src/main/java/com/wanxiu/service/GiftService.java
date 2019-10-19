package com.wanxiu.service;

import com.wanxiu.common.ServiceRes;
import com.wanxiu.dto.*;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveHongbao;
import com.wanxiu.entity.LiveHongbaoLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftService {
    List<GiftDTO> getGiftList();



    List<GIftWithComboDTO> getGiftByTypeList(int type);

    String giftPriceCoin(String id);

    String giftPrice(String id);

    List<GuardianDTO> getGuardianList();

    String guardianPrice(String id);

    String guardianPriceCoin(String id);

    /**
     * 购买礼物
     *
     * @param userId 谁买的
     * @param showId 哪个直播间买的
     * @param giftId 哪个礼物
     * @param count 买几个
     * @return
     */
    ServiceRes<Object> buyGift(String userId, Integer showId, String giftId, int count,boolean ifPrivateRoom);

    ResEntity buyGuardian(String  userId, int showId, String projectId);

    LiveHongbao saveHongBao(LiveHongbao liveHongbao);

    LiveHongbaoLog saveHongBaoLog(LiveHongbaoLog liveHongbaoLog);

    List<HongbaoLogDTO> getHongBaoList(String hongbaoId);
}

