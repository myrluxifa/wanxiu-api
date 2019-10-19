package com.wanxiu.service.impl;

import com.wanxiu.dto.CommentDTO;
import com.wanxiu.entity.LiveMoments;
import com.wanxiu.entity.LiveMomentsComment;
import com.wanxiu.entity.LiveMomentsLike;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.mapper.MomentsMapper;
import com.wanxiu.repository.LiveMomentsCommentRepository;
import com.wanxiu.repository.LiveMomentsLikeRepository;
import com.wanxiu.repository.LiveMomentsRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.service.MomentsService;
import com.wanxiu.util.PagePlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class MomentsServiceImpl implements MomentsService {
    @Autowired
    private LiveMomentsRepository liveMomentsRepository;

    @Autowired
    private LiveMomentsCommentRepository liveMomentsCommentRepository;

    @Autowired
    private LiveMomentsLikeRepository liveMomentsLikeRepository;

    @Autowired
    private MomentsMapper momentsMapper;

    @Autowired
    private LiveUserRepository liveUserRepository;

    public List<LiveMoments> getMomentsList(String userId, String page, String pageSize){
        return liveMomentsRepository.findByCreateUserAndStatusOrderByCreateTimeDesc(PagePlugin.pagePlugin(Integer.valueOf(page),Integer.valueOf(pageSize)),userId,0);
    }

    public LiveMoments addMoments(String userId,String content,String images ){
        LiveMoments liveMoments=new LiveMoments(content,images,userId,0,0,0,new Date());
        return liveMomentsRepository.save(liveMoments);
    }

    public boolean likes(String userId,String id){
        Optional<LiveMoments> optionalLiveMoments=liveMomentsRepository.findById(id);
        if(optionalLiveMoments.isPresent()){
            LiveMoments liveMoments=optionalLiveMoments.get();
            String idol=liveMoments.getCreateUser();
            liveMomentsLikeRepository.save(new LiveMomentsLike(userId,idol,id));
            int likeCnt=liveMomentsLikeRepository.countByMomentsId(id);
            liveMoments.setLikeCnt(likeCnt);
            liveMomentsRepository.save(liveMoments);

            Optional<LiveUser> userOpt = liveUserRepository.findById(idol);
            userOpt.get().setPraise(userOpt.get().getPraise() + 1);
            liveUserRepository.save(userOpt.get());

            return true;
        }else{
            return false;
        }
    }

    public boolean comment(String userId,String id,String comment,String commentId){
        if(StringUtils.isNotBlank(commentId)){//@某条评论
            Optional<LiveMomentsComment> liveMomentsCommentOptional=liveMomentsCommentRepository.findById(commentId);
            if(liveMomentsCommentOptional.isPresent()){
                LiveMomentsComment liveMomentsComment=liveMomentsCommentOptional.get();
                String callUser=liveMomentsComment.getCreateUser();
                LiveMomentsComment liveMomentsComment1=new LiveMomentsComment(comment,0,userId,new Date(),callUser,id);
                liveMomentsCommentRepository.save(liveMomentsComment1);
                return true;
            }else{
                return false;
            }
        }else{
            LiveMomentsComment liveMomentsComment=new LiveMomentsComment(comment,0,userId,new Date(),id);
            liveMomentsCommentRepository.save(liveMomentsComment);
            return true;
        }


    }

    public List<CommentDTO> commentList(String id, String page, String pageSize){
        List<LiveMomentsComment> liveMomentsComments=liveMomentsCommentRepository.findByMomentId(id,PagePlugin.pagePlugin(Integer.valueOf(page),Integer.valueOf(pageSize)));
        List<CommentDTO> commentDTOS=new ArrayList<>();
        for (LiveMomentsComment liveMomentsComment : liveMomentsComments) {

            String callUser="";
            if(StringUtils.isNotBlank(liveMomentsComment.getCreateUser())){
                Optional<LiveUser> optionalLiveUser=liveUserRepository.findById(liveMomentsComment.getCreateUser());
                if(optionalLiveUser.isPresent()){
                    callUser=optionalLiveUser.get().getNickname();
                }
            }
            liveMomentsComment.setCallUser(callUser);
            CommentDTO comment=momentsMapper.getComment(liveMomentsComment,String.valueOf(liveMomentsComment.getCreateTime().getTime()));
            commentDTOS.add(comment);
        }
        return commentDTOS;
    }


    public void updateCommentCnt(String momentId){
        Optional<LiveMoments> optionalLiveMoments=liveMomentsRepository.findById(momentId);
        if(optionalLiveMoments.isPresent()){
            LiveMoments liveMoments=optionalLiveMoments.get();
            liveMoments.setCommentCnt(liveMomentsCommentRepository.countByMomentId(momentId));
            liveMomentsRepository.save(liveMoments);
        }
    }

}
