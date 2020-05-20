package com.finance.papertrader.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trader")
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "user"  // username column in portfolio table
    )
    private List<Portfolio> portfolios = new ArrayList<>();

    protected User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    @Override
    public String toString() {
        return String.format(
                "User [username = %s, portfolios = %s]",
                this.getUsername(),
                this.getPortfolios().toString()
        );
    }
}
