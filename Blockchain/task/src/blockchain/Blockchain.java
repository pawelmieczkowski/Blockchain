package blockchain;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class Blockchain {
    private static final long serialVersionUID = 0L;

    private long msgId;
    private List<Message> messages;
    private List<Message> messagesLocked;
    private final List<Block> ledger;
    private volatile int zeroes;
    private final int reward = 100;

    private final int ZEROES_LOWER_BOUNDARY = 1;
    private final int ZEROES_UPPER_BOUNDARY = 60;

    public Blockchain() {
        this.msgId = 1;
        this.ledger = new ArrayList<>();
        this.zeroes = 3;
        this.messages = new ArrayList<>();
        this.messagesLocked = this.messages;
    }

    private synchronized void addBlock(Block block, long size) {
        if (ledger.size() != size) {
            return;
        }
//        if (block.getTimeToGenerate() < ZEROES_LOWER_BOUNDARY) {
//            zeroes++;
//        }
//        } else if (block.getTimeToGenerate() > ZEROES_UPPER_BOUNDARY) {
//            zeroes--;
//        }
        ledger.add(block);
        messagesLocked = messages;
        messages = new ArrayList<>();
    }

    public synchronized void addMessage(Message message) {
        messages.add(message);
    }

    public void calculateBlock(Miner miner) {
        long startTimeGenerating = System.nanoTime();
        long size = ledger.size();
        String hash;
        Random generator = new Random();
        long magicNumber;
        long timeStamp;
        String prefix = "0".repeat(zeroes);
        List<Message> messagesForBlock = new ArrayList<>(messagesLocked);


        Message minerReward1;
        long nextTransactionId = msgId++;
        Transaction t = new Transaction(nextTransactionId, reward, null, miner.getId());


        try {
            minerReward1 = new Message(t, miner);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            minerReward1 = null;
            e.printStackTrace();
        }
        if (minerReward1 != null) {
            messagesForBlock.add(minerReward1);
        }
        long id;
        String previousHash;

        if (ledger.size() > 0) {
            Block previousBlock = ledger.get(ledger.size() - 1);
            id = previousBlock.getId() + 1;
            previousHash = previousBlock.getHash();
        } else {
            id = 1;
            previousHash = "0";
        }

        do {
            timeStamp = new Date().getTime();
            magicNumber = generator.nextInt(10000000);
            String valueToCalculateHash = id + previousHash + timeStamp + magicNumber + messagesForBlock;
            hash = StringUtil.applySha256(valueToCalculateHash);
            if (!(size == ledger.size())) {
                return;
            }
        } while (!hash.startsWith(prefix));
        int timeToGenerate = (int) (System.nanoTime() - startTimeGenerating) / 1000000000;
        Block block = new Block(miner, id, timeStamp, previousHash, hash, magicNumber, timeToGenerate, messagesForBlock, reward);
        addBlock(block, size);
    }


    public boolean validateBlockchain() {
        if (ledger.isEmpty() || ledger.size() == 1) {
            return true;
        }
        String previousHash = ledger.get(0).getHash();
        List<Message> msg;

        for (Block block : ledger) {
            String hash = block.getHash();
            msg = block.getData();
            if (!hash.equals(previousHash)) {
                return false;
            }
            if (!validateMessages(msg)) {
                return false;
            }
            previousHash = hash;
        }
        return true;
    }

    public boolean validateMessages(List<Message> list) {
        for (Message msg : list) {
            try {
                if (!msg.verifySignature()) {
                    System.out.println("Message invalid");
                    return false;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public int getLedgerSize() {
        return this.ledger.size();
    }

    public void printLedger(long n) {
        n = n > ledger.size() ? ledger.size() : n;
        for (int i = 0; i < n; i++) {
            Block block = ledger.get(i);
            block.print();
            System.out.println("N was " + (block.getTimeToGenerate() < ZEROES_LOWER_BOUNDARY ? "increased to by 1" :
                    block.getTimeToGenerate() > ZEROES_UPPER_BOUNDARY ? "decreased by 1" : "stays the same"));
            System.out.println();
        }
    }

    public long generateMsgId() {
        List<Message> lastBlockMessages = ledger.get(ledger.size() - 1).getData();
        msgId = lastBlockMessages.get(lastBlockMessages.size() - 1).getTransaction().getId() + 1;
        return msgId;
    }
}
