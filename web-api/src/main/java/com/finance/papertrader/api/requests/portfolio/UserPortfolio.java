package com.finance.papertrader.api.requests.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPortfolio {
    private Integer portfolioId;
    private String portfolioName;
}
