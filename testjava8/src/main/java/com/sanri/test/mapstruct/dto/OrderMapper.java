package com.sanri.test.mapstruct.dto;
import com.sanri.test.mapstruct.param.OrderQueryParam;
import com.sanri.test.mapstruct.po.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderQueryParam entity2queryParam(Order order);
}