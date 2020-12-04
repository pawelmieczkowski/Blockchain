package blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public class User {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private final UUID id;

    public User() {
        this.id = UUID.randomUUID();
        try {
            GenerateKeys gk = new GenerateKeys(1024);
            gk.createKeys();
            this.privateKey = gk.getPrivateKey();
            this.publicKey = gk.getPublicKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public UUID getId() {
        return id;
    }
}
