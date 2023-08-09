package com.example.test.Config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassGen {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		System.out.println(encoder.encode("oanh"));
	}
}

//$2a$10$YsmygV/AEOEGFxUwgbhZ1ewqqjSKBtVipKme8Oyd7f6xSEFX7XMjy
