package com.kiffcode.banking.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.kiffcode.banking.domain.Transaction;
import com.kiffcode.banking.domain.*; // for static metamodels
import com.kiffcode.banking.repository.TransactionRepository;
import com.kiffcode.banking.service.dto.TransactionCriteria;

import com.kiffcode.banking.service.dto.TransactionDTO;
import com.kiffcode.banking.service.mapper.TransactionMapper;

/**
 * Service for executing complex queries for Transaction entities in the database.
 * The main input is a {@link TransactionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TransactionDTO} or a {@link Page} of {%link TransactionDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TransactionQueryService extends QueryService<Transaction> {

    private final Logger log = LoggerFactory.getLogger(TransactionQueryService.class);


    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;
    public TransactionQueryService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Return a {@link List} of {%link TransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> findByCriteria(TransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Transaction> specification = createSpecification(criteria);
        return transactionMapper.toDto(transactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link TransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findByCriteria(TransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Transaction> specification = createSpecification(criteria);
        final Page<Transaction> result = transactionRepository.findAll(specification, page);
        return result.map(transactionMapper::toDto);
    }

    /**
     * Function to convert TransactionCriteria to a {@link Specifications}
     */
    private Specifications<Transaction> createSpecification(TransactionCriteria criteria) {
        Specifications<Transaction> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Transaction_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Transaction_.description));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDate(), Transaction_.date));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transaction_.amount));
            }
            if (criteria.getSenderAccountId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSenderAccountId(), Transaction_.senderAccount, AccountUser_.id));
            }
            if (criteria.getReceiverAccountId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReceiverAccountId(), Transaction_.receiverAccount, AccountUser_.id));
            }
        }
        return specification;
    }

}
