package ufrn.imd.cardeasy.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.cardeasy.errors.AccountNotFound;
import ufrn.imd.cardeasy.errors.EmailAlreadyInUse;
import ufrn.imd.cardeasy.errors.PasswordsNotMatch;
import ufrn.imd.cardeasy.errors.Unauthorized;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.repositories.AccountsRepository;
import ufrn.imd.cardeasy.security.Authenticator;
import ufrn.imd.cardeasy.storages.AvatarStorage;

@Service
public class AccountsService {
  private AvatarStorage avatars;
  private Authenticator authenticator;
  private AccountsRepository accounts;
  private BCryptPasswordEncoder encoder;

  @Autowired
  public AccountsService(
    AvatarStorage avatars,
    Authenticator authenticator,
    AccountsRepository accounts,
    BCryptPasswordEncoder encoder
  ) {
    this.avatars = avatars;
    this.authenticator = authenticator;
    this.accounts = accounts;
    this.encoder = encoder;
  };

  @Transactional
  public Account create(
    String name,
    String email,
    String password,
    MultipartFile avatar
  ) {
    if(this.accounts.findByEmail(email).isPresent())
      throw new EmailAlreadyInUse();

    Account account = new Account();
    account.setName(name);
    account.setEmail(email);
    account.setPassword(
      this.encoder.encode(password)
    );

    this.accounts.save(account);

    if(avatar != null)
      this.avatars.store(account.getId(), avatar);

    return account;
  };

  public Account findById(UUID id) {
    return this.accounts.findById(id)
      .orElseThrow(AccountNotFound::new);
  };

  public Account findByEmail(String email) {
    return this.accounts.findByEmail(email)
      .orElseThrow(AccountNotFound::new);
  };

  @Transactional
  public Account update(
    UUID id,
    String name,
    String email,
    String password,
    String newPassword,
    MultipartFile avatar
  ) {
    Optional<Account> candidate = this.accounts.findByEmail(email);

    if(
      candidate.isPresent() &&
      !candidate.get().getId().equals(id)
    ) throw new EmailAlreadyInUse();

    Account account = this.findById(id);

    if(!this.encoder.matches(
      password,
      account.getPassword()
    )) throw new PasswordsNotMatch();

    account.setName(name);
    account.setEmail(email);

    if(newPassword != null)
      account.setPassword(
        this.encoder.encode(newPassword)
      );

    this.accounts.save(account);

    if(avatar != null) {
      this.avatars.store(account.getId(), avatar);
    } else if(this.avatars.exists(account.getId())) {
      this.avatars.delete(account.getId());
    };

    return account;
  };

  public String authenticate(
    String email,
    String password
  ) {
    Account account = this.findByEmail(email);

    if(!this.encoder.matches(
      password,
      account.getPassword()
    )) throw new Unauthorized();

    return this.authenticator.encrypt(
      account.getId()
    );
  };

  public Account verify(
    String token
  ) {
    UUID id = this.authenticator.verify(token);
    return this.findById(id);
  };

  public void existsById(UUID id){
    if(!this.accounts.existsById(id))
      throw new AccountNotFound();
  }
};
