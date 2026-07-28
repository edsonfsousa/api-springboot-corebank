package com.corebank.apispringbootcorebank.presentation.controller;

import com.corebank.apispringbootcorebank.application.input.CreateTransactionInput;
import com.corebank.apispringbootcorebank.application.output.CreateTransactionOutput;
import com.corebank.apispringbootcorebank.application.usecase.CreateTransactionUseCase;
import com.corebank.apispringbootcorebank.presentation.dto.request.CreateTransactionRequest;
import com.corebank.apispringbootcorebank.presentation.dto.response.ApiErrorResponse;
import com.corebank.apispringbootcorebank.presentation.dto.response.CreateTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Transactions", description = "Operations for account transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }

    @PostMapping(
            value = "/{accountId}/transactions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create transaction",
            description = "Creates a debit transaction and returns the account balance after the operation."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Transaction created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTransactionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "transactionId": "0f58155e-192a-4c9e-b548-ecad6f18a95d",
                                      "accountId": "550e8400-e29b-41d4-a716-446655440000",
                                      "amount": 150.00,
                                      "balance": 850.00,
                                      "description": "Compra supermercado",
                                      "createdAt": "2026-07-28T02:30:00Z"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid account identifier or request body",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-07-28T02:30:00Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "amount: Amount must be greater than zero",
                                      "path": "/api/v1/accounts/550e8400-e29b-41d4-a716-446655440000/transactions"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Transaction rejected by a business rule, such as insufficient balance",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<CreateTransactionResponse> create(
            @Parameter(
                    description = "Account UUID",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID accountId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Transaction data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTransactionRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "amount": 150.00,
                                      "description": "Compra supermercado"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        CreateTransactionInput input = new CreateTransactionInput(
                accountId,
                request.amount(),
                request.description()
        );

        CreateTransactionOutput output = createTransactionUseCase.execute(input);

        CreateTransactionResponse response = new CreateTransactionResponse(
                output.transactionId(),
                output.accountId(),
                output.amount(),
                output.balance(),
                output.description(),
                output.createdAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}