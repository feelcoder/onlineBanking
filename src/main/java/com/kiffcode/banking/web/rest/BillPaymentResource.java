package com.kiffcode.banking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kiffcode.banking.service.BillPaymentService;
import com.kiffcode.banking.web.rest.util.HeaderUtil;
import com.kiffcode.banking.web.rest.util.PaginationUtil;
import com.kiffcode.banking.service.dto.BillPaymentDTO;
import com.kiffcode.banking.service.dto.BillPaymentCriteria;
import com.kiffcode.banking.service.BillPaymentQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BillPayment.
 */
@RestController
@RequestMapping("/api")
public class BillPaymentResource {

    private final Logger log = LoggerFactory.getLogger(BillPaymentResource.class);

    private static final String ENTITY_NAME = "billPayment";

    private final BillPaymentService billPaymentService;
    private final BillPaymentQueryService billPaymentQueryService;

    public BillPaymentResource(BillPaymentService billPaymentService, BillPaymentQueryService billPaymentQueryService) {
        this.billPaymentService = billPaymentService;
        this.billPaymentQueryService = billPaymentQueryService;
    }

    /**
     * POST  /bill-payments : Create a new billPayment.
     *
     * @param billPaymentDTO the billPaymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billPaymentDTO, or with status 400 (Bad Request) if the billPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bill-payments")
    @Timed
    public ResponseEntity<BillPaymentDTO> createBillPayment(@Valid @RequestBody BillPaymentDTO billPaymentDTO) throws URISyntaxException {
        log.debug("REST request to save BillPayment : {}", billPaymentDTO);
        if (billPaymentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new billPayment cannot already have an ID")).body(null);
        }
        BillPaymentDTO result = billPaymentService.save(billPaymentDTO);
        return ResponseEntity.created(new URI("/api/bill-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bill-payments : Updates an existing billPayment.
     *
     * @param billPaymentDTO the billPaymentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billPaymentDTO,
     * or with status 400 (Bad Request) if the billPaymentDTO is not valid,
     * or with status 500 (Internal Server Error) if the billPaymentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bill-payments")
    @Timed
    public ResponseEntity<BillPaymentDTO> updateBillPayment(@Valid @RequestBody BillPaymentDTO billPaymentDTO) throws URISyntaxException {
        log.debug("REST request to update BillPayment : {}", billPaymentDTO);
        if (billPaymentDTO.getId() == null) {
            return createBillPayment(billPaymentDTO);
        }
        BillPaymentDTO result = billPaymentService.save(billPaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, billPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bill-payments : get all the billPayments.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of billPayments in body
     */
    @GetMapping("/bill-payments")
    @Timed
    public ResponseEntity<List<BillPaymentDTO>> getAllBillPayments(BillPaymentCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get BillPayments by criteria: {}", criteria);
        Page<BillPaymentDTO> page = billPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bill-payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bill-payments/:id : get the "id" billPayment.
     *
     * @param id the id of the billPaymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billPaymentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bill-payments/{id}")
    @Timed
    public ResponseEntity<BillPaymentDTO> getBillPayment(@PathVariable Long id) {
        log.debug("REST request to get BillPayment : {}", id);
        BillPaymentDTO billPaymentDTO = billPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(billPaymentDTO));
    }

    /**
     * DELETE  /bill-payments/:id : delete the "id" billPayment.
     *
     * @param id the id of the billPaymentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bill-payments/{id}")
    @Timed
    public ResponseEntity<Void> deleteBillPayment(@PathVariable Long id) {
        log.debug("REST request to delete BillPayment : {}", id);
        billPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
