package com.company.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RewardControllerTest extends AbstractControllerTest {

    @Test
    public void shouldReturnRewardForUser() throws Exception {

        mockMvc.perform(get("/rewards").accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
