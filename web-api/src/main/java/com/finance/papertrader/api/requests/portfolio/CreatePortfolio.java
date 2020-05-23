package com.finance.papertrader.api.requests.portfolio;

import lombok.Data;

import java.math.BigDecimal;

public class CreatePortfolio {

    @Data
    public static class Request {
        private String username;
        private String portfolioName;
        private BigDecimal startingAmount;
    }

    @Data
    public static class Response {
        private Integer portfolioId;

        public Response(Integer portfolioId) {
            this.portfolioId = portfolioId;
        }
    }

}
