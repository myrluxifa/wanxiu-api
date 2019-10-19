package com.wanxiu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "广告")
public class BannerDTO {
    @ApiModelProperty(value = "编号")
    private String id;
    @ApiModelProperty(value = "0:跳转地址指向url;1:富文本web_editor")
    private String flag;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value="地址")
    private String url;
    @ApiModelProperty(value = "富文本")
    private String webEditor;
    @ApiModelProperty(value = "图片")
    private String image;

    private String roomType;
}
