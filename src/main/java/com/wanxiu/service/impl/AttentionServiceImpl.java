package com.wanxiu.service.impl;

import com.google.gson.Gson;
import com.wanxiu.config.TencentSMSConfig;
import com.wanxiu.dto.AttentionDTO;
import com.wanxiu.entity.LiveFans;
import com.wanxiu.entity.LiveSysLevel;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.mapper.AttentionMapper;
import com.wanxiu.repository.LiveFansRepository;
import com.wanxiu.repository.LiveSysLevelRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.roomservice.common.Config;
import com.wanxiu.roomservice.pojo.Response.GetStreamStatusRsp;
import com.wanxiu.service.AttentionService;
import com.wanxiu.util.PagePlugin;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class AttentionServiceImpl implements AttentionService {
    @Autowired
    private LiveFansRepository liveFansRepository;

    @Autowired
    private LiveSysLevelRepository liveSysLevelRepository;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private TencentSMSConfig tencentSMSConfig;

    private Logger logger=LoggerFactory.getLogger(AttentionServiceImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Override
    public boolean attention(String idolId, String userId) {
        try{
            LiveFans liveFans=new LiveFans();
            liveFans.setFans(userId);
            liveFans.setCreateTime(new Date());
            liveFans.setIdol(idolId);
            liveFans=liveFansRepository.save(liveFans);
            updateFansCnt(idolId);
            if(liveFans.getId()==null){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean cancelAttention(String idolId, String userId) {
        Optional<LiveFans> optionalLiveFans =liveFansRepository.findByFansAndIdol(userId,idolId);
        if(optionalLiveFans.isPresent()){
            try {
                liveFansRepository.deleteById(optionalLiveFans.get().getId());
                updateFansCnt(idolId);
                return true;
            }catch (Exception e){
                return false;
            }
        }else {
            return true;
        }
    }

    public void updateFansCnt(String idolId){
        int fansCnt=liveFansRepository.countByIdol(idolId);
        Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(idolId);
        if(optionalLiveUser.isPresent()){
            LiveUser liveUser=optionalLiveUser.get();
            liveUser.setFans(fansCnt);
            liveUserRepository.save(liveUser);
        }
    }

    @Override
    public List<AttentionDTO> getAttentionList(String userId,String page,String pageSize) {

        List<Object[]> liveFansList=liveFansRepository.findByFans(PagePlugin.pageFromLimit(Integer.valueOf(page),Integer.valueOf(pageSize)),Integer.valueOf(pageSize),userId);
        List<AttentionDTO> attentionDTOList=new ArrayList<>();
        for (Object[] o : liveFansList) {
            LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(Integer.valueOf(o[7]==null?"0":String.valueOf(o[7])));
            String levelName=liveSysLevel.getName();
            Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(String.valueOf(o[2]==null?"":o[2]));
            String status="0";
            String showId="";
            String lastShowTime="";
            if(optionalLiveUser.isPresent()){
                showId=String.valueOf(optionalLiveUser.get().getShowId());
                lastShowTime=String.valueOf(optionalLiveUser.get().getLastShowTime()==null?"":optionalLiveUser.get().getLastShowTime().getTime());
                GetStreamStatusRsp getStreamStatusRsp=getStatus(showId);
                if(getStreamStatusRsp.getRet()==0){
                    status=String.valueOf(getStreamStatusRsp.getOutput().get(0).getStatus());
                }
            }
            attentionDTOList.add(attentionMapper.getAttention(String.valueOf(o[2]==null?"":o[2]),
                    levelName,
                    status
                    ,String.valueOf(o[4]==null?"":o[4])
                    ,String.valueOf(o[8]==null?"":o[8])
                    ,String.valueOf(o[6]==null?"":o[6])
                    ,String.valueOf(o[5]==null?"":o[5])
                    ,showId
                    ,lastShowTime
                    ));
        }
        return attentionDTOList;
    }

    @Override
    public List<AttentionDTO> getFansList(String userId,String page,String pageSize) {

        List<Object[]> liveFansList=liveFansRepository.findByIdol(PagePlugin.pageFromLimit(Integer.valueOf(page),Integer.valueOf(pageSize)),Integer.valueOf(pageSize),userId);
        List<AttentionDTO> attentionDTOList=new ArrayList<>();
        for (Object[] o : liveFansList) {
            LiveSysLevel liveSysLevel=liveSysLevelRepository.getLevelByEx(Integer.valueOf(o[7]==null?"0":String.valueOf(o[7])));
            String levelName=liveSysLevel.getName();
            Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(String.valueOf(o[1]==null?"":o[1]));
            String status="0";
            String showId="";
            String lastShowTime="";
            if(optionalLiveUser.isPresent()){
                showId=String.valueOf(optionalLiveUser.get().getShowId());
                lastShowTime=String.valueOf(optionalLiveUser.get().getLastShowTime()==null?"":optionalLiveUser.get().getLastShowTime().getTime());
                GetStreamStatusRsp getStreamStatusRsp=getStatus(showId);
                if(getStreamStatusRsp.getRet()==0){
                    status=String.valueOf(getStreamStatusRsp.getOutput().get(0).getStatus());
                }
            }
            attentionDTOList.add(attentionMapper.getAttention(String.valueOf(o[1]==null?"":o[1]),
                    levelName,
                    status
                    ,String.valueOf(o[4]==null?"":o[4])
                    ,String.valueOf(o[8]==null?"":o[8])
                    ,String.valueOf(o[6]==null?"":o[6])
                    ,String.valueOf(o[5]==null?"":o[5])
                    ,showId
                    ,lastShowTime
            ));
        }
        return attentionDTOList;
    }



    public GetStreamStatusRsp getStatus(String showId){

        long time=System.currentTimeMillis()/1000;
        long endTime=time+60;

        String s=restTemplate.getForObject("http://fcgi.video.qcloud.com/common_access?" +
                "appid="+ Config.Live.APP_ID +
                "&interface=Live_Channel_GetStatus" +
                "&Param.s.channel_id="+showId+
                "&t="+String.valueOf(endTime)+
                "&sign="+DigestUtils.md5Hex(Config.Live.APIKEY+String.valueOf(endTime)),String.class);
        GetStreamStatusRsp getStreamStatusRsp=gson.fromJson(s,GetStreamStatusRsp.class);
        logger.info(s);
        return getStreamStatusRsp;
    }
}
