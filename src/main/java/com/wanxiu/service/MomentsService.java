package com.wanxiu.service;

import com.wanxiu.dto.CommentDTO;
import com.wanxiu.entity.LiveMoments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MomentsService {
    List<LiveMoments> getMomentsList(String userId, String page, String pageSize);

    LiveMoments addMoments(String userId,String content,String images );

    boolean likes(String userId,String id);

    boolean comment(String userId,String id,String comment,String commentId);

    List<CommentDTO> commentList(String id,String page,String pageSize);

    void updateCommentCnt(String momentId);
}
