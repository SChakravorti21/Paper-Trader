package com.finance.papertrader.api.requests.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public class SellStock {

    @Data
    public static class Request {
        private Integer holdingId;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private Integer portfolioId;
        private BigDecimal cash;
    }

}
