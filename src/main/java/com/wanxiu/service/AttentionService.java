package com.wanxiu.service;

import com.wanxiu.dto.AttentionDTO;
import com.wanxiu.roomservice.pojo.Response.GetStreamStatusRsp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttentionService {

   boolean attention(String idolId,String userId);

   boolean cancelAttention(String idolId,String userId);

   List<AttentionDTO> getAttentionList(String userId, String page, String pageSize);

    List<AttentionDTO> getFansList(String userId, String page, String pageSize);

    GetStreamStatusRsp getStatus(String showId);
}
