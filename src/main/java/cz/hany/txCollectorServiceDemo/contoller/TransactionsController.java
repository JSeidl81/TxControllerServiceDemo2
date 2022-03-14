package cz.hany.txCollectorServiceDemo.contoller;

import cz.hany.txCollectorServiceDemo.exception.DuplicateRecordException;
import cz.hany.txCollectorServiceDemo.exception.InvalidFormatException;
import cz.hany.txCollectorServiceDemo.exception.RecordNotFoundException;
import cz.hany.txCollectorServiceDemo.model.Transaction;
import cz.hany.txCollectorServiceDemo.repository.TransactionsRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class TransactionsController {
    private final TransactionsRepository repository;

    public TransactionsController(TransactionsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transactions")
    public List<Transaction> all() {
        return repository.findAll();
    }

    @GetMapping("/transactions/id/{id}")
    public Transaction getById(@PathVariable Long id) {
        return repository.getTransactionById(id)
            .orElseThrow(() -> new RecordNotFoundException("Transaction with id: " + id + " not found."));
    }

    @GetMapping("/transactions/actor/{actor}")
    public List<Transaction> allByActor(@PathVariable String actor) { return repository.getAllTransactionsByActor(actor); }

    @GetMapping("/transactions/type/{type}")
    public List<Transaction> allByType(@PathVariable String type) { return repository.getAllTransactionsByType(type); }

    @GetMapping("/transactions/interval/{startTimeStr}/{endTimeStr}")
    public List<Transaction> allByInterval(@PathVariable String startTimeStr, @PathVariable String endTimeStr) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr);

            if (!startTime.isBefore(endTime)) {
                throw new InvalidFormatException("Start of time interval is after end time.");
            }

            return repository.getAllTransactionsByTimeInterval(startTime, endTime);
        } catch (DateTimeParseException dtpe) {
            throw new InvalidFormatException("Invalid parameter format. Valid format is yyyy-mm-ddThh:mm:ss.");
        }
    }

    @PostMapping("/transactions")
    public Transaction newTransaction(@RequestBody Transaction newTransaction) {
        if (!repository.getTransactionById(newTransaction.getTransactionId()).isPresent()) {
            return repository.save(newTransaction);
        } else {
            throw new DuplicateRecordException("Transaction with id: " + newTransaction.getTransactionId() + " is already stored.");
        }
    }

    @DeleteMapping("/transactions/{id}")
    void deleteTransaction(@PathVariable Long id) {
            repository.deleteByTransactionId(id);
    }

}
