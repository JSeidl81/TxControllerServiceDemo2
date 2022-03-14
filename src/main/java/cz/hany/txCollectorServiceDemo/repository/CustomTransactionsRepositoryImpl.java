package cz.hany.txCollectorServiceDemo.repository;

import cz.hany.txCollectorServiceDemo.exception.RecordNotFoundException;
import cz.hany.txCollectorServiceDemo.model.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CustomTransactionsRepositoryImpl implements CustomTransactionsRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(entityManager);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        Optional<Transaction> result = Optional.empty();
        try {
            result = Optional.of((Transaction) entityManager.createQuery("FROM Transaction t WHERE t.transactionId = :id")
                    .setParameter("id", id)
                    .getSingleResult());

        } catch (NoResultException nre) {
            // do nothing
        }

        return result;
    }

    @Override
    public List<Transaction> getAllTransactionsByActor(String actor) {
        return entityManager.createQuery("FROM Transaction t WHERE t.actor = :actor")
                .setParameter("actor", actor)
                .getResultList();
    }

    @Override
    public List<Transaction> getAllTransactionsByType(String type) {
        return entityManager.createQuery("FROM Transaction t WHERE t.type = :type")
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public List<Transaction> getAllTransactionsByTimeInterval(LocalDateTime startTime, LocalDateTime endTime) {
        return entityManager.createQuery("FROM Transaction t WHERE t.time >= :startTime AND t.time <= :endTime")
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteByTransactionId(Long id) {
        Transaction tx = getTransactionById(id)
                .orElseThrow(() -> new RecordNotFoundException("Transaction with id: " + id + " not found."));
        entityManager.remove(tx);
    }
}
