package blockchain;

import java.util.List;

public class Miner extends User implements Runnable {
    private final int minerNumber;
    Blockchain blockchain;


    public Miner(int minerNumber, Blockchain blockchain) {
        super();
        this.minerNumber = minerNumber;
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            blockchain.calculateBlock(this);
        }
    }

    public int getMinerNumber() {
        return minerNumber;
    }
}
