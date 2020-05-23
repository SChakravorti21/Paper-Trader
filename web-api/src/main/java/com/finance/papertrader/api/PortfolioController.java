package com.finance.papertrader.api;

import com.finance.papertrader.api.requests.portfolio.BuyStock;
import com.finance.papertrader.api.requests.portfolio.CreatePortfolio;
import com.finance.papertrader.api.requests.portfolio.SellStock;
import com.finance.papertrader.api.requests.portfolio.UserPortfolio;
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
import java.util.stream.Collectors;

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

    @GetMapping(path = "/list")
    public List<UserPortfolio> listUserPortfolios(@RequestParam String username) {
        Optional<User> user = this.userRepository.findByUsernameEquals(username);

        if (user.isPresent()) {
            return user.get().getPortfolios()
                    .stream()
                    .map(p -> new UserPortfolio(p.getId(), p.getPortfolioName()))
                    .collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    @Transactional
    @PostMapping(path = "/create")
    public CreatePortfolio.Response createPortfolio(@RequestBody CreatePortfolio.Request request) {
        Optional<User> _user = this.userRepository.findById(request.getUsername());

        if (!_user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Portfolio portfolio = Portfolio.builder()
                .portfolioName(request.getPortfolioName())
                .startingAmount(request.getStartingAmount())
                .cash(request.getStartingAmount())
                .user(_user.get())
                .build();

        portfolio = this.portfolioRepository.save(portfolio);
        return new CreatePortfolio.Response(portfolio.getId());
    }

    @Transactional
    @PostMapping(path = "/buy")
    public BuyStock.Response buyStock(@RequestBody BuyStock.Request request) {
        // Look up the portfolio and purchase the stock with available cash
        Optional<Portfolio> _portfolio = this.portfolioRepository.findById(request.getPortfolioId());

        if (!_portfolio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }

        Portfolio portfolio = _portfolio.get();
        PortfolioHolding holding = PortfolioHolding.builder()
                .ticker(request.getTicker())
                .quantity(request.getQuantity())
                // TODO: set purchase price properly once ticker data can be scraped
                .purchasePrice(BigDecimal.valueOf(153.48))
                .portfolio(portfolio)
                .build();

        // Emulate purchase of securities, maybe add transaction fees?
        portfolio.spend(holding.getTotalPurchasePrice());

        // If funds become negative, user does not have enough cash to buy the
        // stock at the desired quantity
        if (portfolio.getCash().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        // Persist changes to the portfolio (holdings set to cascade on persist)
        portfolio = this.portfolioRepository.save(portfolio);
        holding = this.holdingRepository.save(holding);

        return BuyStock.Response.builder()
                .holdingId(holding.getId())
                .portfolioId(portfolio.getId())
                .purchasePrice(holding.getPurchasePrice())
                .quantity(holding.getQuantity())
                .ticker(holding.getTicker())
                .build();
    }

    @Transactional
    @PostMapping(path = "/sell")
    public SellStock.Response sellStock(@RequestBody SellStock.Request request) {
        // TODO: set current price properly once ticker data can be scraped
        BigDecimal currentPrice = BigDecimal.valueOf(164.97);

        // Look up the portfolio and purchase the stock with available cash
        Optional<PortfolioHolding> _holding = this.holdingRepository.findById(request.getHoldingId());

        if (!_holding.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio holding not found");
        }

        PortfolioHolding holding = _holding.get();
        BigDecimal quantity = BigDecimal.valueOf(holding.getQuantity());
        BigDecimal sellPrice = currentPrice.multiply(quantity);
        Portfolio portfolio = holding.getPortfolio();
        portfolio.credit(sellPrice);

        // Persist changes to the portfolio (remove this holding and persist credit)
        this.holdingRepository.delete(holding);
        portfolio = this.portfolioRepository.save(portfolio);
        return new SellStock.Response(portfolio.getId(), portfolio.getCash());
    }

}
