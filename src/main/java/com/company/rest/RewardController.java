package com.company.rest;

import com.company.exception.StandardError;
import com.company.model.RewardsResponse;
import com.company.properties.RewardRules;
import com.company.service.RewardService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rewards")
@AllArgsConstructor
public class RewardController {
    private RewardRules rewardRules;

    private RewardService rewardService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public RewardsResponse getAll() {
        return new RewardsResponse(rewardService.getAll());
    }



}
