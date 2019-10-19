package com.wanxiu.mapper;

import com.wanxiu.dto.CashWithrawalDTO;
import com.wanxiu.entity.LiveCashWithdrawal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface CashWithrawalMapper {

    @Mappings({
            @Mapping(source = "time",target = "time")
    })
    CashWithrawalDTO getCashWithrawal(LiveCashWithdrawal liveCashWithdrawal,String time);
}
