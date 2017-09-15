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

import com.kiffcode.banking.domain.Vendor;
import com.kiffcode.banking.domain.*; // for static metamodels
import com.kiffcode.banking.repository.VendorRepository;
import com.kiffcode.banking.service.dto.VendorCriteria;

import com.kiffcode.banking.service.dto.VendorDTO;
import com.kiffcode.banking.service.mapper.VendorMapper;

/**
 * Service for executing complex queries for Vendor entities in the database.
 * The main input is a {@link VendorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link VendorDTO} or a {@link Page} of {%link VendorDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class VendorQueryService extends QueryService<Vendor> {

    private final Logger log = LoggerFactory.getLogger(VendorQueryService.class);


    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;
    public VendorQueryService(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    /**
     * Return a {@link List} of {%link VendorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VendorDTO> findByCriteria(VendorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Vendor> specification = createSpecification(criteria);
        return vendorMapper.toDto(vendorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link VendorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VendorDTO> findByCriteria(VendorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Vendor> specification = createSpecification(criteria);
        final Page<Vendor> result = vendorRepository.findAll(specification, page);
        return result.map(vendorMapper::toDto);
    }

    /**
     * Function to convert VendorCriteria to a {@link Specifications}
     */
    private Specifications<Vendor> createSpecification(VendorCriteria criteria) {
        Specifications<Vendor> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Vendor_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Vendor_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Vendor_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Vendor_.email));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Vendor_.companyName));
            }
            if (criteria.getIsRegistered() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRegistered(), Vendor_.isRegistered));
            }
            if (criteria.getExpiringDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpiringDate(), Vendor_.expiringDate));
            }
        }
        return specification;
    }

}
