package com.wanxiu.api;

import com.google.gson.Gson;
import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveMinivideo;
import com.wanxiu.entity.LiveMinivideoComment;
import com.wanxiu.entity.LiveMinivideoLike;
import com.wanxiu.entity.LiveUser;
import com.wanxiu.repository.LiveMinivideoCommentRepository;
import com.wanxiu.repository.LiveMinivideoLikeRepository;
import com.wanxiu.repository.LiveMinivideoRepository;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Api(tags = {"小视频"})
@RequestMapping("api/video")
@RestController
public class VideoAPI {

    @Autowired
    LiveMinivideoRepository minivideoRepository;

    @Autowired
    LiveMinivideoCommentRepository commentRepository;

    @Autowired
    LiveUserRepository userRepository;

    @Autowired
    LiveMinivideoLikeRepository minivideoLikeRepository;

    @ApiOperation(value = "上传")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="poster",value="缩略图地址",required = true),
            @ApiImplicitParam(paramType = "query",name="url",value="视频地址",required = true),
            @ApiImplicitParam(paramType = "query",name="comment",value="描述",required = true),
            @ApiImplicitParam(paramType = "query",name="userId",value="userId",required = true)
    })
    @PostMapping("save")
    public ResEntity<Object> save(String poster, String url, String comment, String userId) {

        LiveMinivideo minivideo = new LiveMinivideo();
        minivideo.setComment(0);
        minivideo.setCreateTime(Calendar.getInstance().getTime());
        minivideo.setCreateUser(userId);
        minivideo.setPosterUrl(poster);
        minivideo.setPraise(0);
        minivideo.setShare(0);
        minivideo.setStatus(0);
        minivideo.setTitle(comment);
        minivideo.setUrl(url);
        minivideo.setViews(0);
        minivideoRepository.save(minivideo);

        return new ResEntity<Object>(Common.RESULT.SUCCESS);
    }

    @ApiOperation(value = "分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="page",value="1",required = true),
            @ApiImplicitParam(paramType = "query",name="pageSize",value="30",required = true),
            @ApiImplicitParam(paramType = "query",name="streamerId",value="主播userid")
    })
    @PostMapping("page")
    public ResEntity<Object> page(Integer page, Integer pageSize, String streamerId, String userId) {

        List<LiveMinivideo> list;

        if (StringUtils.isBlank(streamerId)) {
            list = minivideoRepository.page((page - 1) * pageSize, pageSize, userId);
        } else {
            list = minivideoRepository.findByCreateUserOrderByCreateTimeDesc(PagePlugin.pagePlugin(page, pageSize), streamerId);
        }

        return new ResEntity<>(Common.RESULT.SUCCESS, list);
    }

    @ApiOperation(value = "评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="userId",required = true),
            @ApiImplicitParam(paramType = "query",name="comment",value="评论内容",required = true),
            @ApiImplicitParam(paramType = "query",name="atUser",value="艾特谁userId"),
            @ApiImplicitParam(paramType = "query",name="videoId",value="视频id",required = true)
    })
    @PostMapping("comment/save")
    public ResEntity<Object> comment(String videoId, String userId, String atUser, String comment) {

        LiveMinivideoComment record = new LiveMinivideoComment();
        record.setAtUser(atUser);
        record.setComment(comment);
        record.setCreateTime(Calendar.getInstance().getTime());
        record.setPraise(0);
        record.setCreateUser(userId);
        record.setVideoId(videoId);

        Optional<LiveMinivideo> videoOpt = minivideoRepository.findById(videoId);
        videoOpt.get().setComment(videoOpt.get().getComment() + 1);
        minivideoRepository.save(videoOpt.get());

      return new ResEntity<Object>(Common.RESULT.SUCCESS, commentRepository.save(record));
    }

    @ApiOperation(value = "评论分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="page",value="1",required = true),
            @ApiImplicitParam(paramType = "query",name="pageSize",value="30",required = true),
            @ApiImplicitParam(paramType = "query",name="videoId",value="视频id")
    })
    @PostMapping("comment/page")
    public ResEntity<Object> commentPage(Integer page, Integer pageSize, String videoId) {

        List<LiveMinivideoComment> comments = commentRepository.findByVideoIdOrderByCreateTimeDesc(PagePlugin.pagePlugin(page, pageSize), videoId);

        return new ResEntity<Object>(Common.RESULT.SUCCESS, comments);
    }

    @ApiOperation(value = "看视频（统计播放量)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="videoId",value="视频id")
    })
    @PostMapping("view")
    public ResEntity<Object> view(String videoId) {

        Optional<LiveMinivideo> videoOpt = minivideoRepository.findById(videoId);
        videoOpt.get().setViews(videoOpt.get().getViews() + 1);

        return new ResEntity<Object>(Common.RESULT.SUCCESS, minivideoRepository.save(videoOpt.get()));
    }

    @ApiOperation(value = "视频点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="videoId",value="视频id")
    })
    @PostMapping("love")
    public ResEntity<Object> love(String videoId, String userId) {

        LiveMinivideoLike like = new LiveMinivideoLike();
        like.setUserId(userId);
        like.setVideoId(videoId);
        like.setCreateTime(Calendar.getInstance().getTime());
        minivideoLikeRepository.save(like);

        Optional<LiveMinivideo> videoOpt = minivideoRepository.findById(videoId);
        videoOpt.get().setPraise(videoOpt.get().getPraise() + 1);

        Optional<LiveUser> userOpt = userRepository.findById(videoOpt.get().getCreateUser());
        userOpt.get().setPraise(userOpt.get().getPraise() + 1);
        userRepository.save(userOpt.get());

        return new ResEntity<Object>(Common.RESULT.SUCCESS, minivideoRepository.save(videoOpt.get()));
    }

    @ApiOperation(value = "视频取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="videoId",value="视频id")
    })
    @PostMapping("hate")
    public ResEntity<Object> hate(String videoId, String userId) {

        minivideoLikeRepository.deleteByVideoIdAndUserId(videoId, userId);

        Optional<LiveMinivideo> videoOpt = minivideoRepository.findById(videoId);
        videoOpt.get().setPraise(videoOpt.get().getPraise() - 1);

        return new ResEntity<Object>(Common.RESULT.SUCCESS, minivideoRepository.save(videoOpt.get()));
    }
}
