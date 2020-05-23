package com.finance.papertrader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "portfolio_holding")
public class PortfolioHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "holding_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    // TODO: Maybe need a ticker table to ensure that valid tickers are stored
    @Column(name = "ticker", nullable = false)
    private String ticker;

    @Min(1)
    @Column(name = "quantity")
    private Integer quantity;

    @DecimalMin(value = "0.01")
    @Column(name = "purchase_price", nullable = false)
    private BigDecimal purchasePrice;

    public BigDecimal getTotalPurchasePrice() {
        return purchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

}
