package com.finance.papertrader.repositories;

import com.finance.papertrader.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsernameEquals(String username);

}
