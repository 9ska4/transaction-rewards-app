package com.company.rest;

import com.company.exception.StandardError;
import com.company.model.CreateTransaction;
import com.company.model.Transaction;
import com.company.model.TransactionId;
import com.company.model.UpdateTransaction;
import com.company.service.TransactionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Transaction get(@PathVariable long id) {
        return transactionService.get(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource created successfully"),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TransactionId create(@Valid @RequestBody CreateTransaction transaction) {
        return new TransactionId(transactionService.create(transaction));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated successfully"),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TransactionId update(@PathVariable long id, @Valid @RequestBody UpdateTransaction transaction) {
        if (transaction.getId() != id) {
            throw new IllegalArgumentException("Missing consistency of transaction id ");
        }
        return new TransactionId(transactionService.update(transaction));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource deleted successfully"),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        transactionService.delete(id);
    }

}
