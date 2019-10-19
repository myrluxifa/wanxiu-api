package com.wanxiu.dto;

import com.wanxiu.entity.LiveUser;
import lombok.Data;

@Data
public class UserCardDTO {
    private String headPortrait;

    private String nickname;

    private String showId;

    private String level;

    private String sex;

    private String leftHeadPortrait;

    private String rightHeadPortrait;

    private String signature;

    private String ifAttention;

    private String attentionCnt;

    private String fansCnt;

    private String receiveCoinsSum;

    private String giveCoinsSum;

    public UserCardDTO(){

    }

    public UserCardDTO(LiveUser liveUser, String level, String leftHeadPortrait, String rightHeadPortrait, String ifAttention, String attentionCnt, String fansCnt, String receiveCoinsSum, String giveCoinsSum) {
        this.headPortrait = liveUser.getHeadPortrait();
        this.nickname = liveUser.getNickname();
        this.showId = String.valueOf(liveUser.getShowId());
        this.level = level;
        this.sex = String.valueOf(liveUser.getSex());
        this.leftHeadPortrait = leftHeadPortrait;
        this.rightHeadPortrait = rightHeadPortrait;
        this.signature = liveUser.getSignature();
        this.ifAttention = ifAttention;
        this.attentionCnt = attentionCnt;
        this.fansCnt = fansCnt;
        this.receiveCoinsSum = receiveCoinsSum;
        this.giveCoinsSum = giveCoinsSum;
    }
}
