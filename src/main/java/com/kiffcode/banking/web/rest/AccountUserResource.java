package com.kiffcode.banking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiffcode.banking.service.AccountUserService;
import com.kiffcode.banking.web.rest.util.HeaderUtil;
import com.kiffcode.banking.service.dto.AccountUserDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AccountUser.
 */
@RestController
@RequestMapping("/api")
public class AccountUserResource {

    private final Logger log = LoggerFactory.getLogger(AccountUserResource.class);

    private static final String ENTITY_NAME = "accountUser";

    private final AccountUserService accountUserService;

    public AccountUserResource(AccountUserService accountUserService) {
        this.accountUserService = accountUserService;
    }

    /**
     * POST  /account-users : Create a new accountUser.
     *
     * @param accountUserDTO the accountUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountUserDTO, or with status 400 (Bad Request) if the accountUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-users")
    @Timed
    public ResponseEntity<AccountUserDTO> createAccountUser(@RequestBody AccountUserDTO accountUserDTO) throws URISyntaxException {
        log.debug("REST request to save AccountUser : {}", accountUserDTO);
        if (accountUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountUser cannot already have an ID")).body(null);
        }
        AccountUserDTO result = accountUserService.save(accountUserDTO);
        return ResponseEntity.created(new URI("/api/account-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-users : Updates an existing accountUser.
     *
     * @param accountUserDTO the accountUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountUserDTO,
     * or with status 400 (Bad Request) if the accountUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the accountUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-users")
    @Timed
    public ResponseEntity<AccountUserDTO> updateAccountUser(@RequestBody AccountUserDTO accountUserDTO) throws URISyntaxException {
        log.debug("REST request to update AccountUser : {}", accountUserDTO);
        if (accountUserDTO.getId() == null) {
            return createAccountUser(accountUserDTO);
        }
        AccountUserDTO result = accountUserService.save(accountUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-users : get all the accountUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountUsers in body
     */
    @GetMapping("/account-users")
    @Timed
    public List<AccountUserDTO> getAllAccountUsers() {
        log.debug("REST request to get all AccountUsers");
        return accountUserService.findAll();
        }

    /**
     * GET  /account-users/:id : get the "id" accountUser.
     *
     * @param id the id of the accountUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/account-users/{id}")
    @Timed
    public ResponseEntity<AccountUserDTO> getAccountUser(@PathVariable Long id) {
        log.debug("REST request to get AccountUser : {}", id);
        AccountUserDTO accountUserDTO = accountUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountUserDTO));
    }

    /**
     * DELETE  /account-users/:id : delete the "id" accountUser.
     *
     * @param id the id of the accountUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountUser(@PathVariable Long id) {
        log.debug("REST request to delete AccountUser : {}", id);
        accountUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
