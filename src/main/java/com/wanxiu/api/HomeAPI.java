package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.BannerDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveSysAppVersion;
import com.wanxiu.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "homeAPI",tags={"主页"})
@RestController
public class HomeAPI {

    @Autowired
    private HomeService homeService;

    private Logger log= LoggerFactory.getLogger(HomeAPI.class);

    @ApiOperation("广告")
    @RequestMapping(value = "/banner",method = RequestMethod.POST)
    public ResEntity<List<BannerDTO>> banner(String roomType){
        return new ResEntity<List<BannerDTO>>(Common.RESULT.SUCCESS,homeService.getBanner(roomType));
    }


    @ApiOperation("获取房间类型集合")
    @RequestMapping(value = "/getRoomTypeList",method = RequestMethod.POST)
    public ResEntity getRoomTypeList(){
        return new ResEntity(Common.RESULT.SUCCESS,homeService.getRoomTypeList());
    }

    @ApiOperation("检验版本")
    @RequestMapping(value = "/checkNewVersion",method = RequestMethod.POST)
    public ResEntity checkNewVersion(String type,String versionNum){
        log.info("版本设备:"+type);
        log.info("版本编号:"+versionNum);
        try {
            List<LiveSysAppVersion> liveSysAppVersions
                    = homeService.getLiveSysAppVersions(type, versionNum);
            if(liveSysAppVersions.size()>0){
                return new ResEntity(Common.RESULT.NEW_VERSION,liveSysAppVersions.get(0).getApkUri());
            }else{
                return new ResEntity(Common.RESULT.SUCCESS);
            }
        }catch(Exception e){
            return new ResEntity(Common.RESULT.SUCCESS);
        }

    }
}
