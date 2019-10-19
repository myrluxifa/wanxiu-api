package com.wanxiu.service;

import com.wanxiu.dto.AnchorInfoDTO;
import org.springframework.stereotype.Service;

@Service
public interface AnchorUserService {

     AnchorInfoDTO anchorLogin(String phone);
     AnchorInfoDTO register(String phone);
}
