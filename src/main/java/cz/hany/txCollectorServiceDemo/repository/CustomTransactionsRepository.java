package cz.hany.txCollectorServiceDemo.repository;

import cz.hany.txCollectorServiceDemo.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomTransactionsRepository {
    Optional<Transaction> getTransactionById(Long id);
    List<Transaction> getAllTransactionsByActor(String actor);
    List<Transaction> getAllTransactionsByType(String type);
    List<Transaction> getAllTransactionsByTimeInterval(LocalDateTime startTime, LocalDateTime endTime);
    void deleteByTransactionId(Long id);
}
