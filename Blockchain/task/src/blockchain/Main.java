package blockchain;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        //TODO serialization
        //TODO interface for adding transactions into the blockchain
        Blockchain blockchain = new Blockchain();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            Miner miner = new Miner(i, blockchain);
            try {
                executor.submit(miner);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (blockchain.getLedgerSize() >= 15) {
                executor.shutdownNow();
                break;
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blockchain.printLedger(15);
    }
}
