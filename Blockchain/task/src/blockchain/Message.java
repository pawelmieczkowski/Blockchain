package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.UUID;

//https://mkyong.com/java/java-digital-signatures-example/
public class Message {

    private final byte[] signature;
    private final PublicKey publicKey;
    private final Transaction transaction;



    public Message(Transaction transaction, User user)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.transaction = transaction;
        this.publicKey = user.getPublicKey();
        this.signature = sign(transaction.toString(), transaction.getId(), user.getPrivateKey());
    }

    public byte[] sign(String data, long msgId, PrivateKey privateKey)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        String valueToGenerateSignature = data + msgId;
        rsa.update(valueToGenerateSignature.getBytes(StandardCharsets.UTF_8));
        return rsa.sign();
    }

    public boolean verifySignature()
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(transaction.toString().getBytes(StandardCharsets.UTF_8));

        return sig.verify(signature);
    }

    public Transaction getData() {
        return transaction;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
