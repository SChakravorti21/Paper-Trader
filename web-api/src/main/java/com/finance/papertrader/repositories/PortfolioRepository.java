package com.finance.papertrader.repositories;

import com.finance.papertrader.models.Portfolio;
import com.finance.papertrader.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioRepository extends CrudRepository<Portfolio, Integer> {

    public List<Portfolio> findAllByUser(User user);

}
