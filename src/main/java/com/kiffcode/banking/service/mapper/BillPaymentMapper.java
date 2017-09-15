package com.kiffcode.banking.service.mapper;

import com.kiffcode.banking.domain.*;
import com.kiffcode.banking.service.dto.BillPaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BillPayment and its DTO BillPaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {VendorMapper.class, AccountUserMapper.class, })
public interface BillPaymentMapper extends EntityMapper <BillPaymentDTO, BillPayment> {

    @Mapping(source = "vendor.id", target = "vendorId")

    @Mapping(source = "accountUser.id", target = "accountUserId")
    BillPaymentDTO toDto(BillPayment billPayment); 

    @Mapping(source = "vendorId", target = "vendor")

    @Mapping(source = "accountUserId", target = "accountUser")
    BillPayment toEntity(BillPaymentDTO billPaymentDTO); 
    default BillPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillPayment billPayment = new BillPayment();
        billPayment.setId(id);
        return billPayment;
    }
}
