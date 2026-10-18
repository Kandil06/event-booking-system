package com.example.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test")
class BookingApplicationTests {

	@Test
	void contextLoads() {
	}

}
