package com.o2o.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayOrderDTO {
    List<Long> orderList;
    String token;
}
