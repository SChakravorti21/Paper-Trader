package com.finance.papertrader.api;

import com.finance.papertrader.models.Portfolio;
import com.finance.papertrader.models.PortfolioHolding;
import com.finance.papertrader.models.User;
import com.finance.papertrader.repositories.PortfolioHoldingRepository;
import com.finance.papertrader.repositories.PortfolioRepository;
import com.finance.papertrader.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/portfolio")
@Transactional(readOnly = true)
public class PortfolioController {

    private UserRepository userRepository;
    private PortfolioRepository portfolioRepository;
    private PortfolioHoldingRepository holdingRepository;

    public PortfolioController(
            UserRepository userRepository,
            PortfolioRepository portfolioRepository,
            PortfolioHoldingRepository holdingRepository
    ) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.holdingRepository = holdingRepository;
    }

    @GetMapping(path = "/all")
    public List<Portfolio> getUserPortfolios(@RequestParam String username) {
        Optional<User> user = this.userRepository.findByUsernameEquals(username);

        if (user.isPresent()) {
            return user.get().getPortfolios();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    @Transactional
    @PostMapping(path = "/create")
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
        portfolio.setCash(portfolio.getStartingAmount());
        return this.portfolioRepository.save(portfolio);
    }

    @Transactional
    @PostMapping(path = "/buy")
    public Portfolio buyStock(@RequestBody PortfolioHolding holding) {
        // TODO: set purchase price properly once ticker data can be scraped
        holding.setPurchasePrice(BigDecimal.valueOf(153.48));

        // Look up the portfolio and purchase the stock with available cash
        Optional<Portfolio> portfolio = this.portfolioRepository.findById(holding.getPortfolio().getId());

        if (!portfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }

        Portfolio updatedPortfolio = portfolio.get();
        updatedPortfolio.spend(holding.getTotalPurchasePrice());

        // If funds become negative, user does not have enough cash to buy the
        // stock at the desired quantity
        if (updatedPortfolio.getCash().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        // Persist changes to the portfolio (holdings set to cascade on persist)
        updatedPortfolio.addHolding(holding);
        return this.portfolioRepository.save(updatedPortfolio);
    }

}
