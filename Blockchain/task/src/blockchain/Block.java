package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.lang.Object;
import java.util.UUID;

public class Block {
    private final Miner miner;
    private final int minerReward;
    private final long id;
    private final long timeStamp;
    private final String previousHash;
    private final String hash;
    private final long magicNumber;
    private final int timeToGenerate;
    private final List<Message> data;

    //TODO builder pattern
    public Block(Miner miner, long id, long timeStamp, String previousHash, String hash, long magicNumber, int timeToGenerate, List<Message> transaction, int minerReward) {
        this.miner = miner;
        this.minerReward = minerReward;
        this.id = id;
        this.timeStamp = timeStamp;
        this.previousHash = previousHash;
        this.hash = hash;
        this.magicNumber = magicNumber;
        this.timeToGenerate = timeToGenerate;
        this.data = transaction;
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + this.miner.getMinerNumber() + "\n" +
                this.miner.getMinerNumber() + " gets " + this.minerReward + " VC\n" +
                "Id: " + this.id + "\n" +
                "Timestamp: " + this.timeStamp + "\n" +
                "Magic number: " + this.magicNumber + "\n" +
                "Hash of the previous block:\n" +
                this.previousHash + "\n" +
                "Hash of the block:\n" +
                this.hash + "\n" +
                "Block data:\n" +
                data.stream()
                        .map(Message::getData)
                        .map(Transaction::toString)
                        .reduce("no messages", (n, m) -> n = n + "\n" + m)
                + "\n" +
                "Block was generating for " + this.timeToGenerate + " seconds";
    }

    public void print() {
        System.out.println(toString());
    }

    public long getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getTimeToGenerate() {
        return timeToGenerate;
    }

    public List<Message> getData() {
        return data;
    }

    public Miner getMiner() {
        return miner;
    }

    public long getMagicNumber() {
        return magicNumber;
    }
}
