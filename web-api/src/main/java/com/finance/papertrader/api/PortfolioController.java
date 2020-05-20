package com.finance.papertrader.api;

import com.finance.papertrader.models.Portfolio;
import com.finance.papertrader.models.User;
import com.finance.papertrader.repositories.PortfolioHoldingRepository;
import com.finance.papertrader.repositories.PortfolioRepository;
import com.finance.papertrader.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/portfolio")
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

    @Transactional
    @GetMapping(path = "/all")
    public List<Portfolio> getUserPortfolios(@RequestParam String username) {
        Optional<User> user = this.userRepository.findByUsernameEquals(username);

        if (user.isPresent()) {
            return user.get().getPortfolios();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    @PostMapping(path = "/create")
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
        return this.portfolioRepository.save(portfolio);
    }

}
