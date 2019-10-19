package com.wanxiu.mapper;

import com.wanxiu.dto.CommentDTO;
import com.wanxiu.dto.LiveMomentsDTO;
import com.wanxiu.entity.LiveMoments;
import com.wanxiu.entity.LiveMomentsComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface MomentsMapper {
    @Mappings({
            @Mapping(source = "time",target = "createTime"),
            @Mapping(source = "ifLike",target = "ifLike")
    })
    LiveMomentsDTO getMoments(LiveMoments liveMoments,String time,String ifLike);


    @Mappings({
            @Mapping(source = "time",target = "createTime")
    })
    CommentDTO getComment(LiveMomentsComment liveMomentsComment,String time);
}
