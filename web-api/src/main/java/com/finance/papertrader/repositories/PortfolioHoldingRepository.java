package com.finance.papertrader.repositories;

import com.finance.papertrader.models.PortfolioHolding;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioHoldingRepository extends CrudRepository<PortfolioHolding, Integer> {
}
