package com.kiffcode.banking.service;

import com.kiffcode.banking.domain.BillPayment;
import com.kiffcode.banking.repository.BillPaymentRepository;
import com.kiffcode.banking.service.dto.BillPaymentDTO;
import com.kiffcode.banking.service.mapper.BillPaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing BillPayment.
 */
@Service
@Transactional
public class BillPaymentService {

    private final Logger log = LoggerFactory.getLogger(BillPaymentService.class);

    private final BillPaymentRepository billPaymentRepository;

    private final BillPaymentMapper billPaymentMapper;
    public BillPaymentService(BillPaymentRepository billPaymentRepository, BillPaymentMapper billPaymentMapper) {
        this.billPaymentRepository = billPaymentRepository;
        this.billPaymentMapper = billPaymentMapper;
    }

    /**
     * Save a billPayment.
     *
     * @param billPaymentDTO the entity to save
     * @return the persisted entity
     */
    public BillPaymentDTO save(BillPaymentDTO billPaymentDTO) {
        log.debug("Request to save BillPayment : {}", billPaymentDTO);
        BillPayment billPayment = billPaymentMapper.toEntity(billPaymentDTO);
        billPayment = billPaymentRepository.save(billPayment);
        return billPaymentMapper.toDto(billPayment);
    }

    /**
     *  Get all the billPayments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BillPaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillPayments");
        return billPaymentRepository.findAll(pageable)
            .map(billPaymentMapper::toDto);
    }

    /**
     *  Get one billPayment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BillPaymentDTO findOne(Long id) {
        log.debug("Request to get BillPayment : {}", id);
        BillPayment billPayment = billPaymentRepository.findOne(id);
        return billPaymentMapper.toDto(billPayment);
    }

    /**
     *  Delete the  billPayment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BillPayment : {}", id);
        billPaymentRepository.delete(id);
    }
}
