package com.simulador.financiero.config.seed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.account.Status;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.entities.TransactionEntity;
import com.simulador.financiero.entities.TransactionStatus;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.AccountRepository;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.repositories.StockRepository;
import com.simulador.financiero.repositories.TransactionRepository;
import com.simulador.financiero.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads a small, deterministic demo data set (users, accounts, stocks, a position and
 * a few transactions) so the API can be demoed and tested manually right away,
 * including trading without depending on Alpha Vantage.
 *
 * Enabled only with {@code app.seed.enabled=true} (env var {@code SEED_DEMO_DATA}).
 * Idempotent: every record is looked up (email, ticker, account number) before it is
 * inserted, so running it again never duplicates data. It runs before the daily quotes
 * batch runner, which may later refresh the stock prices with real quotes.
 */
@Component
@Slf4j
@Order(1)
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

    static final String DEMO_PASSWORD = "Demo1234!";

    static final String ANA_EMAIL = "demo.ana@example.com";
    static final String LUIS_EMAIL = "demo.luis@example.com";

    // Account numbers: 16 digits, "3141" prefix like the numbers the app generates.
    static final String ANA_MXN = "3141000000000001";
    static final String ANA_USD = "3141000000000002";
    static final String LUIS_MXN = "3141000000000003";
    static final String LUIS_BLOCKED = "3141000000000004";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final ActionRepository actionRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(
            UserRepository userRepository,
            AccountRepository accountRepository,
            StockRepository stockRepository,
            ActionRepository actionRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.actionRepository = actionRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        UserEntity ana = findOrCreateUser(
                "Ana Demo", "DEAN900515MDFMRN08", ANA_EMAIL, "5511111111");
        UserEntity luis = findOrCreateUser(
                "Luis Demo", "DELU880722HDFMSS03", LUIS_EMAIL, "5522222222");

        boolean anaMxnIsNew = !accountRepository.existsById(ANA_MXN);
        boolean luisMxnIsNew = !accountRepository.existsById(LUIS_MXN);

        AccountEntity anaMxn = findOrCreateAccount(
                ANA_MXN, ana, AccountType.Checking, Currency.MXN, "50000.00", Status.ACTIVE);
        AccountEntity anaUsd = findOrCreateAccount(
                ANA_USD, ana, AccountType.Saving, Currency.USD, "3000.00", Status.ACTIVE);
        AccountEntity luisMxn = findOrCreateAccount(
                LUIS_MXN, luis, AccountType.Checking, Currency.MXN, "20000.00", Status.ACTIVE);
        findOrCreateAccount(
                LUIS_BLOCKED, luis, AccountType.Saving, Currency.MXN, "1000.00", Status.BLOCKED);

        seedStock("AAPL", "Apple Inc.", "189.84");
        seedStock("MSFT", "Microsoft Corporation", "415.20");
        seedStock("TSLA", "Tesla, Inc.", "248.50");
        seedStock("AMZN", "Amazon.com, Inc.", "178.25");
        seedStock("WMT", "Walmart Inc.", "60.10");

        seedPosition(anaUsd, "AAPL", 5, "185.50");

        // History is seeded only together with the accounts it belongs to, so the stored
        // balances (already the final numbers) stay consistent with it.
        if (anaMxnIsNew && luisMxnIsNew) {
            seedHistory(anaMxn, luisMxn);
        }

        log.info("Demo data ready (SEED_DEMO_DATA=true). Password for all demo users: {} | "
                + "{} -> accounts {} (MXN), {} (USD, holds 5 AAPL) | "
                + "{} -> accounts {} (MXN), {} (MXN, BLOCKED) | stocks: AAPL, MSFT, TSLA, AMZN, WMT",
                DEMO_PASSWORD, ANA_EMAIL, ANA_MXN, ANA_USD, LUIS_EMAIL, LUIS_MXN, LUIS_BLOCKED);
    }

    private UserEntity findOrCreateUser(String fullName, String curp, String email, String phone) {
        return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(
                UserEntity.builder()
                        .fullName(fullName)
                        .curp(curp)
                        .email(email)
                        .password(passwordEncoder.encode(DEMO_PASSWORD))
                        .phone(phone)
                        .build()));
    }

    private AccountEntity findOrCreateAccount(
            String number, UserEntity owner, AccountType type, Currency currency,
            String balance, Status status) {
        return accountRepository.findById(number).orElseGet(() -> accountRepository.save(
                AccountEntity.builder()
                        .number(number)
                        .user(owner)
                        .accountType(type)
                        .currency(currency)
                        .balance(new BigDecimal(balance))
                        .status(status)
                        .build()));
    }

    private void seedStock(String ticker, String companyName, String price) {
        if (stockRepository.findByTickerIgnoreCase(ticker).isPresent()) {
            return;
        }
        StocksEntity stock = new StocksEntity();
        stock.setTicker(ticker);
        stock.setCompanyName(companyName);
        stock.setCurrentPrice(new BigDecimal(price));
        stock.setUpdatedAt(LocalDateTime.now());
        stockRepository.save(stock);
    }

    private void seedPosition(AccountEntity account, String ticker, int quantity, String averagePrice) {
        StocksEntity stock = stockRepository.findByTickerIgnoreCase(ticker).orElseThrow();
        if (actionRepository.findByAccountAndStock(account, stock).isEmpty()) {
            actionRepository.save(ActionEntity.builder()
                    .account(account)
                    .stock(stock)
                    .quantity(quantity)
                    .average(new BigDecimal(averagePrice))
                    .build());
        }
    }

    /**
     * Ana MXN: +60,000.00 deposit, -8,000.00 withdrawal, -2,000.00 transfer to Luis = 50,000.00.
     * Luis MXN: +18,000.00 deposit, +2,000.00 transfer from Ana = 20,000.00.
     */
    private void seedHistory(AccountEntity anaMxn, AccountEntity luisMxn) {
        LocalDateTime now = LocalDateTime.now();
        transactionRepository.save(selfMovement(
                anaMxn, "60000.00", "Initial deposit", now.minusDays(10)));
        transactionRepository.save(selfMovement(
                luisMxn, "18000.00", "Initial deposit", now.minusDays(9)));
        transactionRepository.save(selfMovement(
                anaMxn, "8000.00", "ATM withdrawal", now.minusDays(5)));
        transactionRepository.save(TransactionEntity.builder()
                .originAccount(anaMxn)
                .destinationAccount(luisMxn)
                .originalAmount(new BigDecimal("2000.00"))
                .originCurrency(anaMxn.getCurrency())
                .destinationAmount(new BigDecimal("2000.00"))
                .destinationCurrency(luisMxn.getCurrency())
                .exchangeRate(BigDecimal.ONE.setScale(6))
                .concept("Rent split")
                .date(now.minusDays(2))
                .status(TransactionStatus.COMPLETED)
                .build());
    }

    /** Same shape the services use for deposits and withdrawals (origin = destination). */
    private TransactionEntity selfMovement(
            AccountEntity account, String amount, String concept, LocalDateTime date) {
        return TransactionEntity.builder()
                .originAccount(account)
                .destinationAccount(account)
                .originalAmount(new BigDecimal(amount))
                .originCurrency(account.getCurrency())
                .destinationAmount(new BigDecimal(amount))
                .destinationCurrency(account.getCurrency())
                .exchangeRate(BigDecimal.ONE)
                .concept(concept)
                .date(date)
                .status(TransactionStatus.COMPLETED)
                .build();
    }
}
