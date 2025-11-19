package ufrn.imd.cardeasy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.dtos.account.AccountDTO;
import ufrn.imd.cardeasy.dtos.account.AuthenticateAccountDTO;
import ufrn.imd.cardeasy.dtos.account.CreateAccountDTO;
import ufrn.imd.cardeasy.dtos.account.UpdateAccountDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AccountsService;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
  private AccountsService service;

  @Autowired
  public AccountsController(
    AccountsService service
  ) {
    this.service = service;
  };

  
  @PostMapping
  @SecurityRequirements
  @Tag(name = "Accounts")
  @Operation(summary = "Create a account")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Account created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class})))
  })
  public ResponseEntity<Void> create(
    @RequestPart(name = "avatar", required = false) MultipartFile avatar,
    @RequestPart("account") @Valid CreateAccountDTO account
  ) {
    this.service.create(
      account.name(),
      account.email(),
      account.password(),
      avatar
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .build();
  };

  @Authenticate
  @PutMapping
  @Tag(name = "Accounts")
  @Operation(summary = "Update a account")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Account updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> update(
    @AuthenticationPrincipal Account account,
    @RequestPart(name = "avatar", required = false) MultipartFile avatar,
    @RequestPart @Valid UpdateAccountDTO body
  ) {
    this.service.update(
      account.getId(),
      body.name(),
      body.email(),
      body.password(),
      body.newPassword(),
      avatar
    );

    return ResponseEntity
      .status(HttpStatus.OK)
      .build();
  };

  @PostMapping("/auth")
  @SecurityRequirements
  @Tag(name = "Authentication")
  @Operation(summary = "Authenticate a account")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Account authenticated", content = @Content(schema = @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNTEyNDJkOC02MTYyLTQxMzYtODgxOS1mZTE2NThkZjgwOGEiLCJpYXQiOjE3NjM1NzU4NDQsImV4cCI6MTc2MzY2MjI0NH0.p9uDOMl9VZan8kpjw-W1d3G47rD8FYknSH_AIPf68Jg"))),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<String> authenticate(
    @RequestBody @Valid AuthenticateAccountDTO body
  ) {
    String token = this.service.authenticate(
      body.email(), 
      body.password()
    );

    return ResponseEntity.ok(token);
  };

  @Authenticate
  @GetMapping("/verify")
  @Tag(name = "Accounts")
  @Operation(summary = "Verify a account")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Account verified"),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<AccountDTO> verify(
    @AuthenticationPrincipal Account account
  ) {
    return ResponseEntity.ok(new AccountDTO(
      account.getId(),
      account.getName(),
      account.getEmail()
    ));
  };
};
