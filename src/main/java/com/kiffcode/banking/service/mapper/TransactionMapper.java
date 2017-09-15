package com.kiffcode.banking.service.mapper;

import com.kiffcode.banking.domain.*;
import com.kiffcode.banking.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {AccountUserMapper.class, })
public interface TransactionMapper extends EntityMapper <TransactionDTO, Transaction> {

    @Mapping(source = "senderAccount.id", target = "senderAccountId")

    @Mapping(source = "receiverAccount.id", target = "receiverAccountId")
    TransactionDTO toDto(Transaction transaction); 

    @Mapping(source = "senderAccountId", target = "senderAccount")

    @Mapping(source = "receiverAccountId", target = "receiverAccount")
    Transaction toEntity(TransactionDTO transactionDTO); 
    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
