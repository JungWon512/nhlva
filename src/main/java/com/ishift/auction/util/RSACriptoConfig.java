package com.ishift.auction.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RSACriptoConfig {

	public String encryptRsa(String plainText, PublicKey publicKey) {
		
		String encValue = "";
		
		try {			
			Cipher cipher= Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE,publicKey);
			byte[] encByte = cipher.doFinal(plainText.getBytes("UTF-8")); 
			return byteArrayToHex(encByte);
		}catch(RuntimeException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		}  catch (NoSuchAlgorithmException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		} catch (NoSuchPaddingException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		} catch (InvalidKeyException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		} catch (IllegalBlockSizeException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		} catch (BadPaddingException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		} catch (UnsupportedEncodingException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		}
		
		return encValue;
	}

	public String decryptRsa(PrivateKey privateKey, String encodeValue) {
		
		String decryptvalue = "";
		
		try {
			
			Cipher cipher = Cipher.getInstance("RSA");			
			byte[] encrpytedBytes = hexToByteArray(encodeValue);			
			cipher.init(cipher.DECRYPT_MODE, privateKey);			
			byte[] decryptedBytes = cipher.doFinal(encrpytedBytes);
			decryptvalue = new String(decryptedBytes, "UTF-8");			
		}catch(RuntimeException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			log.error("RSACriptoConfig.decryptRsa : {} ",e);
		}
		
		return decryptvalue;
	}
	
	public static byte[] hexToByteArray(String hex) {
		
		if(hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}
		
		byte[] bytes = new byte[hex.length() / 2];
		
		for(int i = 0; i < hex.length(); i +=2) {
			
			byte value = (byte)Integer.parseInt(hex.substring(i, i +2), 16);
			bytes[(int)Math.floorDiv(i, 2)] = value;
		}
		
		return bytes;
	}

	public String byteArrayToHex(byte[] ba) {
		if(ba == null || ba.length == 0) return null;
		StringBuffer sb = new StringBuffer();
		String hexNumber = "";
		
		for(int x = 0 ; x< ba.length;x++) {
			hexNumber = "0"+Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length()-2));
		}
		
		return sb.toString();
	}


	public PrivateKey StringToPrivateKey(String str) {
		PrivateKey privateKey = null;
		try {
			PKCS8EncodedKeySpec rkeySpec = new PKCS8EncodedKeySpec(hexToByteArray(str));
			KeyFactory rKeyFactory = KeyFactory.getInstance("RSA");
			privateKey = rKeyFactory.generatePrivate(rkeySpec);
		}catch (RuntimeException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("RSACriptoConfig.StringToPrivateKey : {} ",e);
		}
		return privateKey;
	}


	public PublicKey StringToPublicKey(String str) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec pKeySpec = new X509EncodedKeySpec(this.hexToByteArray(str));
			KeyFactory pKeyFactory = KeyFactory.getInstance("RSA");
			publicKey = pKeyFactory.generatePublic(pKeySpec);
		}catch (RuntimeException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("RSACriptoConfig.StringToPublicKey : {} ",e);
		}
		return publicKey;
	}
		
	
	
}
