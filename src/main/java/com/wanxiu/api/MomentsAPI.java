package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.CommentDTO;
import com.wanxiu.dto.LiveMomentsDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveMoments;
import com.wanxiu.entity.LiveMomentsComment;
import com.wanxiu.mapper.MomentsMapper;
import com.wanxiu.repository.LiveMomentsCommentRepository;
import com.wanxiu.repository.LiveMomentsLikeRepository;
import com.wanxiu.service.MomentsService;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Api(value = "momentAPI",tags = {"动态"})
@RestController
public class MomentsAPI {

    @Autowired
    private MomentsService momentsService;

    @Autowired
    private MomentsMapper momentsMapper;

    @Autowired
    private LiveMomentsLikeRepository liveMomentsLikeRepository;

    @Autowired
    private LiveMomentsCommentRepository commentRepository;

    @RequestMapping(value = "/momentsList",method = RequestMethod.POST)
    public ResEntity momentsList(String userId,String page,String pageSize,String clientUserId){

        List<LiveMoments> momentsList=momentsService.getMomentsList(userId,page,pageSize);
        List<LiveMomentsDTO> momentsDTOList=new ArrayList<>();
        for(LiveMoments liveMoments:momentsList){
            int count=liveMomentsLikeRepository.countByMomentsIdAndUserId(liveMoments.getId(),clientUserId);
            momentsDTOList.add( momentsMapper.getMoments(liveMoments,String.valueOf(liveMoments.getCreateTime().getTime()),count>0?"true":"false"));
        }
        return new ResEntity(Common.RESULT.SUCCESS,momentsDTOList);
    }

    @RequestMapping(value="/addMoments",method = RequestMethod.POST)
    public ResEntity addMoments(String userId,String content,String images){
        LiveMoments liveMoments=momentsService.addMoments(userId,content,images);
        return new ResEntity(Common.RESULT.SUCCESS,momentsMapper.getMoments(liveMoments,String.valueOf(liveMoments.getCreateTime().getTime()),"false"));
    }

    @RequestMapping(value="/likes",method = RequestMethod.POST)
    public ResEntity likes(String userId,String id){


        if(momentsService.likes(userId,id)){
            return new ResEntity(Common.RESULT.SUCCESS);
        }else{
            return new ResEntity(Common.RESULT.FAIL);
        }
    }

    @ApiOperation(value = "评论")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType="query", name = "userId", value = "用户编号", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "id", value = "动态编号", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "comment", value = "评论", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "commentId", value = "被回复的评论编号", required = true, dataType = "String")
            }
    )
    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public ResEntity comment(String userId,String id,String comment,String commentId){
        if(momentsService.comment(userId,id,comment,commentId)){
            momentsService.updateCommentCnt(id);
            return new ResEntity(Common.RESULT.SUCCESS);
        }else{
            return new ResEntity(Common.RESULT.FAIL);
        }
    }

    @ApiOperation(value = "评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="id",value = "动态编号",required = true)
    })
    @PostMapping("/commentList2")
    public ResEntity<Object> commentPage(String id, Integer page, Integer pageSize) {
        List<LiveMomentsComment> result = commentRepository.findByMomentIdOrderByCreateTimeDesc(id, PagePlugin.pagePlugin(page, pageSize));
        return new ResEntity<>(Common.RESULT.SUCCESS, result);
    }

    @ApiOperation(value = "评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="id",value = "动态编号",required = true)
    })
    @RequestMapping(value="/commentList",method = RequestMethod.POST)
    public ResEntity commentList(String id,String page,String pageSize){
        List<CommentDTO> commentDTOS=momentsService.commentList(id,page,pageSize);
        return new ResEntity(Common.RESULT.SUCCESS,commentDTOS);
    }



}
