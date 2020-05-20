package com.finance.papertrader.repositories;

import com.finance.papertrader.models.Portfolio;
import org.springframework.data.repository.CrudRepository;


public interface PortfolioRepository extends CrudRepository<Portfolio, Integer> {
}
