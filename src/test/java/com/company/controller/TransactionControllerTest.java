package com.company.controller;

import com.company.exception.TransactionNotFoundException;
import com.company.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends AbstractControllerTest {

    @Test
    public void shouldReturnTransaction() throws Exception {

        Instant instant = Instant.now();
        Transaction existingTransaction = Transaction.builder()
                .id(1)
                .userId("a123")
                .amount(BigDecimal.valueOf(100.34))
                .created(instant)
                .build();

        when(transactionService.get(1L))
                .thenReturn(existingTransaction);

        mockMvc.perform(get("/transactions/1").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("a123")))
                .andExpect(jsonPath("$.amount", is(100.34)))
                .andExpect(jsonPath("$.created", is(instant.toString())));
    }

    @Test
    public void shouldReturnErrorWhenTransactionNotExists() throws Exception {

        String errorMessage = "lorem ipsum failed";
        TransactionNotFoundException notFoundException = new TransactionNotFoundException(errorMessage);

        when(transactionService.get(1L))
                .thenThrow(notFoundException);

        mockMvc.perform(get("/transactions/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    public void shouldCreateTransaction() throws Exception {

        String validRequestBody = """
                {
                    "userId": "a123",
                    "amount": 100.19,
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(Long.valueOf(1));

        mockMvc.perform(post("/transactions")
                        .content(validRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void shouldReturnErrorOnCreateTransactionWithIllegalUserIdCharacters() throws Exception {

        String invalidUserIdRequestBody = """
                {
                    "userId": "{console.log(1)}",
                    "amount": 100.19
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(1L);

        mockMvc.perform(post("/transactions")
                        .content(invalidUserIdRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorOnCreateTransactionWithoutAmount() throws Exception {

        String invalidRequestBody = """
                {
                    "userId": "a123",
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(1L);

        mockMvc.perform(post("/transactions")
                        .content(invalidRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorOnCreateTransactionWithoutZeroAmount() throws Exception {

        String invalidRequestBody = """
                {
                    "userId": "a123",
                    "amount": 0.00,
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(1L);

        mockMvc.perform(post("/transactions")
                        .content(invalidRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorOnCreateNegativeAmountTransaction() throws Exception {

        String negativeAmountRequestBody = """
                {
                    "userId": "a123",
                    "amount": -100.19,
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(1L);

        mockMvc.perform(post("/transactions")
                        .content(negativeAmountRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnErrorOnCreateTooPreciseAmountTransaction() throws Exception {

        String tooPreciseAmountRequestBody = """
                {
                    "userId": "a123",
                    "amount": 100.19111111,
                    "created": "2023-03-20T20:10:30Z"
                }""";

        when(transactionService.create(any()))
                .thenReturn(1L);

        mockMvc.perform(post("/transactions")
                        .content(tooPreciseAmountRequestBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
