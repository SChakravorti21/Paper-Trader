package com.finance.papertrader.api.requests.portfolio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public class BuyStock {

    @Data
    public static class Request {
        private Integer portfolioId;
        private String ticker;
        private Integer quantity;
    }

    @Data
    @Builder
    public static class Response {
        private Integer holdingId;
        private Integer portfolioId;
        private String ticker;
        private Integer quantity;
        private BigDecimal purchasePrice;
    }

}
