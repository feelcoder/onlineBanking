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

import com.kiffcode.banking.domain.BillPayment;
import com.kiffcode.banking.domain.*; // for static metamodels
import com.kiffcode.banking.repository.BillPaymentRepository;
import com.kiffcode.banking.service.dto.BillPaymentCriteria;

import com.kiffcode.banking.service.dto.BillPaymentDTO;
import com.kiffcode.banking.service.mapper.BillPaymentMapper;

/**
 * Service for executing complex queries for BillPayment entities in the database.
 * The main input is a {@link BillPaymentCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link BillPaymentDTO} or a {@link Page} of {%link BillPaymentDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class BillPaymentQueryService extends QueryService<BillPayment> {

    private final Logger log = LoggerFactory.getLogger(BillPaymentQueryService.class);


    private final BillPaymentRepository billPaymentRepository;

    private final BillPaymentMapper billPaymentMapper;
    public BillPaymentQueryService(BillPaymentRepository billPaymentRepository, BillPaymentMapper billPaymentMapper) {
        this.billPaymentRepository = billPaymentRepository;
        this.billPaymentMapper = billPaymentMapper;
    }

    /**
     * Return a {@link List} of {%link BillPaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillPaymentDTO> findByCriteria(BillPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BillPayment> specification = createSpecification(criteria);
        return billPaymentMapper.toDto(billPaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link BillPaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillPaymentDTO> findByCriteria(BillPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BillPayment> specification = createSpecification(criteria);
        final Page<BillPayment> result = billPaymentRepository.findAll(specification, page);
        return result.map(billPaymentMapper::toDto);
    }

    /**
     * Function to convert BillPaymentCriteria to a {@link Specifications}
     */
    private Specifications<BillPayment> createSpecification(BillPaymentCriteria criteria) {
        Specifications<BillPayment> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BillPayment_.id));
            }
            if (criteria.getScheduledDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getScheduledDate(), BillPayment_.scheduledDate));
            }
            if (criteria.getFrequencyMode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrequencyMode(), BillPayment_.frequencyMode));
            }
            if (criteria.getPaymentComplete() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentComplete(), BillPayment_.paymentComplete));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), BillPayment_.amount));
            }
            if (criteria.getVendorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVendorId(), BillPayment_.vendor, Vendor_.id));
            }
            if (criteria.getAccountUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAccountUserId(), BillPayment_.accountUser, AccountUser_.id));
            }
        }
        return specification;
    }

}
