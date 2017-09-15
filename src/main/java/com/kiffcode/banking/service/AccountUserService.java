package com.kiffcode.banking.service;

import com.kiffcode.banking.service.dto.AccountUserDTO;
import java.util.List;

/**
 * Service Interface for managing AccountUser.
 */
public interface AccountUserService {

    /**
     * Save a accountUser.
     *
     * @param accountUserDTO the entity to save
     * @return the persisted entity
     */
    AccountUserDTO save(AccountUserDTO accountUserDTO);

    /**
     *  Get all the accountUsers.
     *
     *  @return the list of entities
     */
    List<AccountUserDTO> findAll();

    /**
     *  Get the "id" accountUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AccountUserDTO findOne(Long id);

    /**
     *  Delete the "id" accountUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
