package com.kiffcode.banking.repository;

import com.kiffcode.banking.domain.AccountUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the AccountUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

    @Query("select account from AccountUser account where account.user.login = ?#{principal.username}")
    List<AccountUser> findByUserIsCurrentUser();

}
