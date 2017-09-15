package com.kiffcode.banking.service.mapper;

import com.kiffcode.banking.domain.*;
import com.kiffcode.banking.service.dto.AccountUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountUser and its DTO AccountUserDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface AccountUserMapper extends EntityMapper <AccountUserDTO, AccountUser> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    AccountUserDTO toDto(AccountUser accountUser); 

    @Mapping(source = "userId", target = "user")
    AccountUser toEntity(AccountUserDTO accountUserDTO); 
    default AccountUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountUser accountUser = new AccountUser();
        accountUser.setId(id);
        return accountUser;
    }
}
