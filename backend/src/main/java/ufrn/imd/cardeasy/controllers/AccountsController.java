package ufrn.imd.cardeasy.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dto.AccountDTO;
import ufrn.imd.cardeasy.dto.AuthenticateAccountDTO;
import ufrn.imd.cardeasy.dto.CreateAccountDTO;
import ufrn.imd.cardeasy.dto.UpdateAccountDTO;
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
  public ResponseEntity<Void> create(
    @RequestPart("avatar") MultipartFile avatar,
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

  @PostMapping("/auth")
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
  public ResponseEntity<AccountDTO> verify(
    @AuthenticationPrincipal Account account
  ) {
    return ResponseEntity.ok(new AccountDTO(
      account.getId(),
      account.getName(),
      account.getEmail()
    ));
  };

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
    @PathVariable UUID id,
    @RequestPart MultipartFile avatar,
    @RequestPart @Valid UpdateAccountDTO account
  ) {
    this.service.update(
      id,
      account.name(),
      account.email(),
      account.password(),
      account.newPassword(),
      avatar
    );

    return ResponseEntity
      .status(HttpStatus.OK)
      .build();
  };
};
