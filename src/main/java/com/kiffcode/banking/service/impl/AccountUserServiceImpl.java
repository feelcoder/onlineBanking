package com.kiffcode.banking.service.impl;

import com.kiffcode.banking.service.AccountUserService;
import com.kiffcode.banking.domain.AccountUser;
import com.kiffcode.banking.repository.AccountUserRepository;
import com.kiffcode.banking.service.dto.AccountUserDTO;
import com.kiffcode.banking.service.mapper.AccountUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AccountUser.
 */
@Service
@Transactional
public class AccountUserServiceImpl implements AccountUserService{

    private final Logger log = LoggerFactory.getLogger(AccountUserServiceImpl.class);

    private final AccountUserRepository accountUserRepository;

    private final AccountUserMapper accountUserMapper;
    public AccountUserServiceImpl(AccountUserRepository accountUserRepository, AccountUserMapper accountUserMapper) {
        this.accountUserRepository = accountUserRepository;
        this.accountUserMapper = accountUserMapper;
    }

    /**
     * Save a accountUser.
     *
     * @param accountUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountUserDTO save(AccountUserDTO accountUserDTO) {
        log.debug("Request to save AccountUser : {}", accountUserDTO);
        AccountUser accountUser = accountUserMapper.toEntity(accountUserDTO);
        accountUser = accountUserRepository.save(accountUser);
        return accountUserMapper.toDto(accountUser);
    }

    /**
     *  Get all the accountUsers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccountUserDTO> findAll() {
        log.debug("Request to get all AccountUsers");
        return accountUserRepository.findAll().stream()
            .map(accountUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one accountUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AccountUserDTO findOne(Long id) {
        log.debug("Request to get AccountUser : {}", id);
        AccountUser accountUser = accountUserRepository.findOne(id);
        return accountUserMapper.toDto(accountUser);
    }

    /**
     *  Delete the  accountUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountUser : {}", id);
        accountUserRepository.delete(id);
    }
}
