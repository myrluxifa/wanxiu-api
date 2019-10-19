package com.wanxiu.service.impl;

import com.wanxiu.api.RankAPI;
import com.wanxiu.common.AccountParam;
import com.wanxiu.common.Common;
import com.wanxiu.common.ServiceRes;
import com.wanxiu.common.SlitMoneyParam;
import com.wanxiu.dto.*;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.*;
import com.wanxiu.mapper.GiftMapper;
import com.wanxiu.mapper.GuardianMapper;
import com.wanxiu.repository.*;
import com.wanxiu.service.*;
import com.wanxiu.util.TimeUtil;
import com.wanxiu.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class GiftServiceImpl implements GiftService {

    @Autowired
    private LiveGiftRepository liveGiftRepository;

    @Autowired
    private LiveSysGuardianRepository liveSysGuardianRepository;

    @Autowired
    private GiftMapper giftMapper;

    @Autowired
    private GuardianMapper guardianMapper;

    @Autowired
    private LiveSysRatioRepository liveSysRatioRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LiveUserRepository userRepository;

    @Autowired
    private LiveAccountRepository accountRepository;

    @Autowired
    private LiveGiftRankRepository giftRankRepository;

    @Autowired
    private LiveUserGuardianRepository userGuardianRepository;

    @Autowired
    private LiveHongbaoRepository liveHongbaoRepository;

    @Autowired
    private LiveHongbaoLogRepository liveHongbaoLogRepository;

    @Autowired
    private SlitMoneyService slitMoneyService;

    @Autowired
    private PayService payService;

    @Autowired
    private LevelService levelService;

    @Override
    public List<GiftDTO> getGiftList() {
        List<LiveGift> liveGifts=liveGiftRepository.findByStatus(0);
        List<GiftDTO> giftDTOList=new ArrayList<>();
        for(LiveGift liveGift:liveGifts){
            giftDTOList.add(giftMapper.getGift(liveGift));
        }
        return giftDTOList;
    }


    public List<GIftWithComboDTO> getGiftByTypeList(int type) {
        List<LiveGift> liveGifts=liveGiftRepository.findByStatusAndGiftType(0,type);
        List<GIftWithComboDTO> giftDTOList=new ArrayList<>();
        for(LiveGift liveGift:liveGifts){
            List<GiftComboDTO> giftComboDTOS=new ArrayList<>();
            if(StringUtils.isNotBlank(liveGift.getFiveTimesUrl())){
                giftComboDTOS.add(new GiftComboDTO("99", Util.convertStringToUTF8(liveGift.getFiveTimesUrl())));
            }
            if(StringUtils.isNotBlank(liveGift.getTenTimesUrl())){
                giftComboDTOS.add(new GiftComboDTO("520",Util.convertStringToUTF8(liveGift.getTenTimesUrl())));
            }
            if(StringUtils.isNotBlank(liveGift.getNinetynineTimesUrl())){
                giftComboDTOS.add(new GiftComboDTO("1314",Util.convertStringToUTF8(liveGift.getNinetynineTimesUrl())));
            }
            GIftWithComboDTO giftWithCombo=giftMapper.getGiftWithCombo(liveGift);
            giftWithCombo.setGiftComboDTOS(giftComboDTOS);
            giftDTOList.add(giftWithCombo);
        }
        return giftDTOList;
    }

    public String giftPriceCoin(String id){
        Optional<LiveGift> liveGiftOptional =liveGiftRepository.findById(id);
        if(liveGiftOptional.isPresent()){
            return String.valueOf(liveGiftOptional.get().getShowCoin());
        }else{
            return "-1";
        }
    }

    public String giftPrice(String id){
        Optional<LiveGift> liveGiftOptional =liveGiftRepository.findById(id);
        LiveSysRatio liveSysRatio=liveSysRatioRepository.findAll().iterator().next();
        if(liveGiftOptional.isPresent()){
            BigDecimal price=new BigDecimal(liveGiftOptional.get().getShowCoin()).divide(new BigDecimal(liveSysRatio.getRatio()));
            return String.valueOf(price);
        }else{
            return "-1";
        }
    }

    public List<GuardianDTO> getGuardianList() {
        List<LiveSysGuardian> liveSysGuardians=liveSysGuardianRepository.findByStatus(0);
        List<GuardianDTO> guardianDTOS=new ArrayList<>();
        for(LiveSysGuardian liveSysGuardian:liveSysGuardians){
            guardianDTOS.add(guardianMapper.getGuardian(liveSysGuardian));
        }
        return guardianDTOS;
    }

    public String guardianPriceCoin(String id){
        Optional<LiveSysGuardian> liveSysGuardian =liveSysGuardianRepository.findById(id);
        if(liveSysGuardian.isPresent()){
            return String.valueOf(liveSysGuardian.get().getShowCoin());
        }else{
            return "-1";
        }
    }

    @Override
    @Transactional
    public ServiceRes<Object> buyGift(String userId, Integer showId, String giftId, int count,boolean ifPrivateRoom) {

        if(count<=0) {
            return new ServiceRes<>().fail("购买数据必须大于0！");
        }

        // 总价
        int price = Integer.valueOf(giftPriceCoin(giftId)) * count;

        if (price < 0) {
            return new ServiceRes<>().fail("礼物不存在！");
        }
        // 主播
        LiveUser streamer = userRepository.findByShowId(showId);

        if(null == streamer) {
            return new ServiceRes<>().fail("主播不存在！");
        }
        // 用户
        Optional<LiveUser> userOpt = userRepository.findById(userId);

        if(!userOpt.isPresent()) {
            return new ServiceRes<>().fail("用户不存在！");
        }
        // 主播账户
        Optional<LiveAccount> streamerAcct = accountRepository.findByUserId(streamer.getId());
        if(!streamerAcct.isPresent()) {
            return new ServiceRes<>().fail("主播账户不存在！");
        }

        // 用户账户
        Optional<LiveAccount> userAcct = accountRepository.findByUserId(userId);
        if(!ifPrivateRoom) {
            if (!userAcct.isPresent()) {
                return new ServiceRes<>().fail("用户账户不存在！");
            }
        }

        // 主播加秀币
        ServiceRes streamerAcctRes = accountService.change(streamerAcct.get().getId(), userId, price, AccountParam.CHANGE_TYPE_FROM_GIFT);
        if(streamerAcctRes.isFail()) {
            return streamerAcctRes;
        }
        ServiceRes userAcctRes = null;
        if(!ifPrivateRoom) {
            // 用户减秀币
            userAcctRes = accountService.change(userAcct.get().getId(), userId, price, AccountParam.CHANGE_TYPE_EXPEND_GIFT);
            if (userAcctRes.isFail()) {
                return userAcctRes;
            }
        }

        //判断守护
        Optional<LiveUserGuardian> guardian = userGuardianRepository.findByUserIdAndTargetId(userId,streamer.getShowId());
        String userType = guardian.isPresent() ? "G": "N";

        // 初始化统计周期
        String dCycleId = RankAPI.getCycleId("D");
        String wCycleId = RankAPI.getCycleId("W");
        String mCycleId = RankAPI.getCycleId("M");

        //统计用户对单一主播贡献
        deposit(showId, userId, dCycleId, wCycleId, mCycleId, price, userType);
        //主播超级榜
        sall(showId, userId, price);
        //统计用户对所有主播贡献 R-富豪榜
        deposit(null, userId, dCycleId, wCycleId, mCycleId, price, "R");
        //统计主播收入 S-明星榜
        deposit(null, streamer.getId(), dCycleId, wCycleId, mCycleId, price, "S");
        //统计用户超级榜
        deposit1(userId, "R", price);
        //统计主播超级榜
        deposit1(streamer.getId(), "S", price);

        slitMoneyService.slitMoney(SlitMoneyParam.SLIT_TYPE_ANCHOR,
                price,
                new BigDecimal(0),
                SlitMoneyParam.USE_TYPE_BUY_GURAD,
                userId,
                String.valueOf(showId)
        );
        levelService.addExp(price,userId,showId);
        if(!ifPrivateRoom) {
            return new ServiceRes<>().success(userAcctRes.getData());
        }else{
            return new ServiceRes<>().success();
        }
    }

    public void deposit1(String userId, String userType, Integer price) {
        LiveGiftRank rank = giftRankRepository.findByUserIdAndCycleIdAndUserType(userId, "ALL", userType);

            if (null == rank) {
            rank = new LiveGiftRank();
            rank.setUserId(userId);
            rank.setCoins(price);
            rank.setCycleId("ALL");
            rank.setType("ALL");
            rank.setUserType(userType);
        } else {
            rank.setUserType(userType);
            rank.setCoins(rank.getCoins() + price);
        }
        giftRankRepository.save(rank);
    }

    // 统计用户给主播刷的数据 区分每个主播
    public void deposit(Integer showId, String userId, String dCycleId, String wCycleId, String mCycleId, Integer price, String userType) {
        // 查询统计数据
        LiveGiftRank day = giftRankRepository.findByShowIdAndUserIdAndUserTypeAndCycleId(showId, userId, userType, dCycleId);

        if (null == day) {
            day = new LiveGiftRank();
            day.setUserId(userId);
            day.setCoins(price);
            day.setCycleId(dCycleId);
            day.setShowId(showId);
            day.setType("D");
            day.setUserType(userType);
        } else {
            day.setUserType(userType);
            day.setCoins(day.getCoins() + price);
        }
        giftRankRepository.save(day);

        LiveGiftRank week = giftRankRepository.findByShowIdAndUserIdAndUserTypeAndCycleId(showId, userId, userType, wCycleId);

        if (null == week) {
            week = new LiveGiftRank();
            week.setUserId(userId);
            week.setCoins(price);
            week.setCycleId(wCycleId);
            week.setShowId(showId);
            week.setType("W");
            week.setUserType(userType);
        } else {
            week.setUserType(userType);
            week.setCoins(week.getCoins() + price);
        }
        giftRankRepository.save(week);

        LiveGiftRank month = giftRankRepository.findByShowIdAndUserIdAndUserTypeAndCycleId(showId, userId, userType, mCycleId);

        if (null == month) {
            month = new LiveGiftRank();
            month.setUserId(userId);
            month.setCoins(price);
            month.setCycleId(mCycleId);
            month.setShowId(showId);
            month.setType("M");
            month.setUserType(userType);
        } else {
            month.setUserType(userType);
            month.setCoins(month.getCoins() + price);
        }

        giftRankRepository.save(month);
    }

    //主播超级榜
    public void sall(Integer showId, String userId, Integer price) {
        //指定主播超级榜
        LiveGiftRank a = giftRankRepository.findByShowIdAndUserIdAndUserTypeAndCycleId(showId, userId, "SALL", "SALL");

        if (null == a) {
            a = new LiveGiftRank();
            a.setUserId(userId);
            a.setCoins(price);
            a.setCycleId("SALL");
            a.setShowId(showId);
            a.setType("SALL");
            a.setUserType("SALL");
        } else {
            a.setUserType("SALL");
            a.setCoins(a.getCoins() + price);
        }

        giftRankRepository.save(a);
    }

    public String guardianPrice(String id){
        Optional<LiveSysGuardian> liveSysGuardian =liveSysGuardianRepository.findById(id);
        LiveSysRatio liveSysRatio=liveSysRatioRepository.findAll().iterator().next();
        if(liveSysGuardian.isPresent()){
            BigDecimal price=new BigDecimal(liveSysGuardian.get().getShowCoin()).divide(new BigDecimal(liveSysRatio.getRatio()));
            return String.valueOf(price);
        }else{
            return "-1";
        }
    }

    @Transactional
    public ResEntity buyGuardian(String  userId, int showId, String projectId){
        Optional<LiveUserGuardian> optionalLiveUserGuardian=userGuardianRepository.findByUserIdAndTargetId(userId,showId);
        Optional<LiveSysGuardian> optionalLiveSysGuardian =liveSysGuardianRepository.findById(projectId);
        LiveSysGuardian liveSysGuardian;
        if(!optionalLiveSysGuardian.isPresent()){
            return new ResEntity(Common.RESULT.PROJECT_ID_404);
        }else{
            liveSysGuardian=optionalLiveSysGuardian.get();
        }

        LiveUser anchorUser=userRepository.findByShowId(showId);

        Optional<LiveAccount> optionalLiveAccountUser=accountRepository.findByUserId(userId);
        if(!optionalLiveAccountUser.isPresent()){
            return new ResEntity(Common.RESULT.ACCOUNT_404);
        }

        Optional<LiveAccount> optionalLiveAccountAnchor=accountRepository.findByUserId(anchorUser.getId());
        if(!optionalLiveAccountAnchor.isPresent()){
            return new ResEntity(Common.RESULT.ACCOUNT_404);
        }

        if(optionalLiveUserGuardian.isPresent()){

            LiveUserGuardian liveUserGuardian=optionalLiveUserGuardian.get();
            Date endDate=liveUserGuardian.getEndTime();
            Date now=new Date();
            //如果守护已经过期则从当前时间计算结束时间
            if(endDate.before(now)){
                endDate=TimeUtil.guardianEndTime(now,liveSysGuardian.getTimeLimit());
            }else{
                endDate=TimeUtil.guardianEndTime(endDate,liveSysGuardian.getTimeLimit());
            }
            liveUserGuardian.setEndTime(endDate);
            userGuardianRepository.saveAndFlush(liveUserGuardian);
        }else{
            userGuardianRepository.save(
                    new LiveUserGuardian(userId,showId,projectId,TimeUtil.guardianEndTime(new Date(),liveSysGuardian.getTimeLimit()))
            );
        }

        //更新账户信息
        int coin = liveSysGuardian.getShowCoin();
        //消费者账户
        ServiceRes userRes=accountService.change(optionalLiveAccountUser.get().getId(), userId, coin, AccountParam.CHANGE_TYPE_EXPEND);
        if(userRes.isFail()){
            return new ResEntity(userRes);
        }
        //主播账户
        ServiceRes anchorRes=accountService.change(optionalLiveAccountAnchor.get().getId(), anchorUser.getId(), coin, AccountParam.CHANGE_TYPE_FROM_GIFT);
        if(anchorRes.isFail()){
            return new ResEntity(userRes);
        }

        LiveOrder order = payService.saveOrder(new LiveOrder(
                userId,//用户编号
                new BigDecimal(0),//支付金额单位为分
                1,//支付方式（1 余额支付 2 微信支付 3支付宝支付）
                2,//支付状态 1未支付 2已支付
                new Date(),
                2,
                anchorUser.getId(),//被刷礼物主播的id
                projectId,//礼物或守护编号
                coin
        ));
        slitMoneyService.slitMoney(SlitMoneyParam.SLIT_TYPE_ANCHOR,
                coin,
                new BigDecimal(0),
                SlitMoneyParam.USE_TYPE_BUY_GURAD,
                userId,
                String.valueOf(showId)
        );

        levelService.addExp(coin,userId,showId);

        return new ResEntity(Common.RESULT.SUCCESS);
    }


    public LiveHongbao saveHongBao(LiveHongbao liveHongbao){
        return liveHongbaoRepository.save(liveHongbao);
    }

    @Override
    public LiveHongbaoLog saveHongBaoLog(LiveHongbaoLog liveHongbaoLog) {
        return liveHongbaoLogRepository.save(liveHongbaoLog);
    }

    @Override
    public List<HongbaoLogDTO> getHongBaoList(String hongbaoId) {
        List<Object[]>  objects=liveHongbaoLogRepository.queryByHongbaoId(hongbaoId);
        List<HongbaoLogDTO> hongbaoLogDTOS=new ArrayList<>();
        for(Object[] o: objects){
                hongbaoLogDTOS.add( new HongbaoLogDTO(o));
        }
        return hongbaoLogDTOS;
    }

    public ResEntity hongBaoReturn(String showId){
        try {
            List<LiveHongbao> hongbaos = liveHongbaoRepository.findByShowIdAndStatus(showId, 0);
            for (LiveHongbao hongbao : hongbaos) {
                String _userId = hongbao.getCreateUser();
                int _sum = liveHongbaoLogRepository.sumHongBao(hongbao.getId());
                if (_sum < hongbao.getAmount()) {
                    String accountId = accountService.getAccountId(_userId);
                    int _returnAmount = hongbao.getAmount() - _sum;
                    accountService.change(accountId, _userId, _returnAmount, AccountParam.CHANGE_TYPE_FROM_HONGBAO_RETURN);

                }
                hongbao.setStatus(1);
            }
            return new ResEntity(Common.RESULT.SUCCESS);
        }catch (Exception e){
            return new ResEntity(Common.RESULT.FAIL);
        }
    }
}