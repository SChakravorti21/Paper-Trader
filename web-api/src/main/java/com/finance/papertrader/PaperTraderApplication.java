package com.finance.papertrader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaperTraderApplication {

    private static final Logger log = LoggerFactory.getLogger(PaperTraderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PaperTraderApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demo(UserRepository userRepository) {
//        return new CommandLineRunner() {
//            @Override
//            @Transactional
//            public void run(String... args) throws Exception {
//                log.info("\nSaving a couple users");
//                userRepository.save(new User("shozimo"));
//
//                log.info("All users in database:");
//                userRepository.findAll().forEach(user -> log.info(" - " + user.getUsername()));
//
//                log.info("Finding a specific user (`shozimo`)...");
//                log.info(userRepository.findByUsernameEquals("shozimo").get().toString());
//                log.info(userRepository.findById("shozimo").get().toString());
//            }
//        };
//    }

}
