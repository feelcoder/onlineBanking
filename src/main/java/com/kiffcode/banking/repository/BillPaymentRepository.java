package com.kiffcode.banking.repository;

import com.kiffcode.banking.domain.BillPayment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BillPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long>, JpaSpecificationExecutor<BillPayment> {

}
