package blockchain;

import java.util.UUID;

public class Transaction {
    long id;
    long amount;
    UUID from;
    UUID to;

    public Transaction(long id, long amount, UUID from, UUID to) {
        this.id = id;
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public UUID getFrom() {
        return from;
    }

    public UUID getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
