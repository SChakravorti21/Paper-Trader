package com.finance.papertrader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    // Need to ignore user when serializing to avoid stack overflow
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "portfolio"  // portfolio_id in portfolio_holding table
    )
    private List<PortfolioHolding> holdings = new ArrayList<>();

    protected Portfolio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PortfolioHolding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<PortfolioHolding> holdings) {
        this.holdings = holdings;
    }

    public void addHolding(@Valid PortfolioHolding holding) {
        this.holdings.add(holding);
    }

    public BigDecimal getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(BigDecimal startingAmount) {
        this.startingAmount = startingAmount;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public void spend(BigDecimal amount) {
        this.cash = this.cash.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        this.cash = this.cash.add(amount);
    }

    @Override
    public String toString() {
        return String.format("Portfolio [id = %d]", this.id);
    }
}
