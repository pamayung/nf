package com.wildhan.nf;

import com.wildhan.nf.core.db.DB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NfApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(NfApplication.class, args);
			System.out.println("Init database connection...");
			DB db = DB.getInstance();
			db.create();
			System.out.println("Init database connection... OK");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
