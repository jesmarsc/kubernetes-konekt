package com.kubernetes.konekt.security;

import java.sql.Blob;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

@Component
public class ClusterSecurity {
	private static String password = "a8d66f8636826d884e63d129b50086bc1360c8af1897b566";

	/*
	 * Takes in credential in plaintext and returns encrypted bytes stored in a blob for storage on database.
	 * During the event that an exception is thrown the method returns null
	 */
	public Blob encodeCredential(String credential) {
		// some salt to avoid dictionary attacks
		String salt = KeyGenerators.string().generateKey();
		// setting up encryptor with password and the salt
		BytesEncryptor encryptor = Encryptors.standard(password, "f7e989fe45e687f0");
		// get encrypted credentials
		credential += salt;
		byte[] encryptedCredentialBytes = encryptor.encrypt(credential.getBytes());
		try {
			Blob encryptedCredentialBlob = new javax.sql.rowset.serial.SerialBlob(encryptedCredentialBytes);
			return encryptedCredentialBlob;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	 * Takes in a blob holding encrypted information and returns the decrypted string
	 * If an exception occurs the method will return null.
	 */
	public String decodeCredential(Blob encryptedBlob) {
		// setting up encryptor with password and the salt
		BytesEncryptor decryptor = Encryptors.standard(password, "f7e989fe45e687f0");
	
		try {
			Integer blobLength = (int) encryptedBlob.length();
			byte[] encryptedData = encryptedBlob.getBytes(1, blobLength);
			byte[] decryptedCredentialsBytes = decryptor.decrypt(encryptedData);
			String decryptedMessage = new String(decryptedCredentialsBytes);
			//removing salt added during encryption
			String credential = decryptedMessage.substring(0, decryptedMessage.length() - 16);
			return credential;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
