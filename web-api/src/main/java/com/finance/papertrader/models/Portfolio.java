package com.finance.papertrader.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "portfolio_id")
    private Integer id;

    @Column(name = "portfolio_name", nullable = false)
    private String portfolioName;

    @DecimalMin(value = "100.0")
    @DecimalMax(value = "500000.0")
    @Column(name = "starting_amount", nullable = false)
    private BigDecimal startingAmount;

    @DecimalMin(value = "0.0")
    @Column(name = "cash", nullable = false)
    private BigDecimal cash;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "portfolio"  // portfolio_id in portfolio_holding table
    )
    private List<PortfolioHolding> holdings;

    public void addHolding(@Valid PortfolioHolding holding) {
        this.holdings.add(holding);
    }

    public void spend(BigDecimal amount) {
        this.cash = this.cash.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        this.cash = this.cash.add(amount);
    }

}
