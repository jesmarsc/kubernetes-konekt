package com.kubernetes.konekt;

import java.sql.Blob;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kubernetes.konekt.security.ClusterSecurity;


@SpringBootTest
public class TestSpringSecurityCrypto {
	

	@Test
	public static void main(String[] args)  {
		

		
		ClusterSecurity clusterSecurity = new ClusterSecurity();
		Blob encryptedMessage = clusterSecurity.encodeCredential("bUr485TM8ehSqVeM" +  
				"");
		System.out.println(encryptedMessage);
		String decryptedMessage = clusterSecurity.decodeCredential(encryptedMessage);
		System.out.println(decryptedMessage);

		
		
	
	}

}
