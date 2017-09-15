package com.kiffcode.banking.service.mapper;

import com.kiffcode.banking.domain.*;
import com.kiffcode.banking.service.dto.VendorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vendor and its DTO VendorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VendorMapper extends EntityMapper <VendorDTO, Vendor> {
    
    
    default Vendor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vendor vendor = new Vendor();
        vendor.setId(id);
        return vendor;
    }
}
