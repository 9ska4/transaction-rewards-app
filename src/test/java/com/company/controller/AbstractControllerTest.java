package com.company.controller;

import com.company.service.RewardService;
import com.company.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected TransactionService transactionService;

	@MockBean
	protected RewardService rewardService;

	@BeforeEach
	public void setUp() {
		Mockito.reset(transactionService);
	}

}
