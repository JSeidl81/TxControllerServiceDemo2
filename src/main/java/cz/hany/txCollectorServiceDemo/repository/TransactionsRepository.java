package cz.hany.txCollectorServiceDemo.repository;

import cz.hany.txCollectorServiceDemo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long>, CustomTransactionsRepository {
}
