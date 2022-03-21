package cz.hany.txCollectorServiceDemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(indexes = {
                @Index(columnList = "transactionId"),
                @Index(columnList = "time"),
                @Index(columnList = "type"),
                @Index(columnList = "actor")
        },
        name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private Long transactionId;
    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime time;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String actor;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="key_name")
    @Column(name="value")
    @CollectionTable(name="transaction_data", joinColumns=@JoinColumn(name="tx_id"))
    Map<String, String> txData = new HashMap<>();

    public Transaction() {}

    public Transaction(Long txId, LocalDateTime time, String type, String actor, Map<String, String> txData) {
        this.transactionId = txId;
        this.time = time;
        this.type = type;
        this.actor = actor;
        this.txData = txData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Map<String, String> getTxData() {
        return txData;
    }

    public void setTxData(Map<String, String> txData) {
        this.txData = txData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Transaction))
            return false;
        Transaction tx = (Transaction) o;
        return Objects.equals(this.id, tx.id)
                && Objects.equals(this.transactionId, tx.transactionId)
                && Objects.equals(this.time, tx.time)
                && Objects.equals(this.type, tx.type)
                && Objects.equals(this.actor, tx.actor)
                && Objects.equals(this.txData, tx.txData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.transactionId, this.time, this.type, this.actor, this.txData);
    }

}
