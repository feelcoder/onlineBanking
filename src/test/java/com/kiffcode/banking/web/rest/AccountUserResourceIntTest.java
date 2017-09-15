package com.kiffcode.banking.web.rest;

import com.kiffcode.banking.OnlineBankingApp;

import com.kiffcode.banking.domain.AccountUser;
import com.kiffcode.banking.repository.AccountUserRepository;
import com.kiffcode.banking.service.AccountUserService;
import com.kiffcode.banking.service.dto.AccountUserDTO;
import com.kiffcode.banking.service.mapper.AccountUserMapper;
import com.kiffcode.banking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountUserResource REST controller.
 *
 * @see AccountUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlineBankingApp.class)
public class AccountUserResourceIntTest {

    private static final String DEFAULT_ACCOUNT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NO = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NO = "BBBBBBBBBB";

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    private static final Long DEFAULT_LIMIT = 1L;
    private static final Long UPDATED_LIMIT = 2L;

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AccountUserRepository accountUserRepository;

    @Autowired
    private AccountUserMapper accountUserMapper;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountUserMockMvc;

    private AccountUser accountUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountUserResource accountUserResource = new AccountUserResource(accountUserService);
        this.restAccountUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountUser createEntity(EntityManager em) {
        AccountUser accountUser = new AccountUser()
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountNo(DEFAULT_ACCOUNT_NO)
            .balance(DEFAULT_BALANCE)
            .limit(DEFAULT_LIMIT)
            .isActivated(DEFAULT_IS_ACTIVATED)
            .createdDate(DEFAULT_CREATED_DATE);
        return accountUser;
    }

    @Before
    public void initTest() {
        accountUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountUser() throws Exception {
        int databaseSizeBeforeCreate = accountUserRepository.findAll().size();

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);
        restAccountUserMockMvc.perform(post("/api/account-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountUserDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeCreate + 1);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountUser.getAccountNo()).isEqualTo(DEFAULT_ACCOUNT_NO);
        assertThat(testAccountUser.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testAccountUser.getLimit()).isEqualTo(DEFAULT_LIMIT);
        assertThat(testAccountUser.isIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
        assertThat(testAccountUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createAccountUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountUserRepository.findAll().size();

        // Create the AccountUser with an existing ID
        accountUser.setId(1L);
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountUserMockMvc.perform(post("/api/account-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountUsers() throws Exception {
        // Initialize the database
        accountUserRepository.saveAndFlush(accountUser);

        // Get all the accountUserList
        restAccountUserMockMvc.perform(get("/api/account-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountNo").value(hasItem(DEFAULT_ACCOUNT_NO.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getAccountUser() throws Exception {
        // Initialize the database
        accountUserRepository.saveAndFlush(accountUser);

        // Get the accountUser
        restAccountUserMockMvc.perform(get("/api/account-users/{id}", accountUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountUser.getId().intValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountNo").value(DEFAULT_ACCOUNT_NO.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT.intValue()))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountUser() throws Exception {
        // Get the accountUser
        restAccountUserMockMvc.perform(get("/api/account-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountUser() throws Exception {
        // Initialize the database
        accountUserRepository.saveAndFlush(accountUser);
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();

        // Update the accountUser
        AccountUser updatedAccountUser = accountUserRepository.findOne(accountUser.getId());
        updatedAccountUser
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountNo(UPDATED_ACCOUNT_NO)
            .balance(UPDATED_BALANCE)
            .limit(UPDATED_LIMIT)
            .isActivated(UPDATED_IS_ACTIVATED)
            .createdDate(UPDATED_CREATED_DATE);
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(updatedAccountUser);

        restAccountUserMockMvc.perform(put("/api/account-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountUserDTO)))
            .andExpect(status().isOk());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountUser.getAccountNo()).isEqualTo(UPDATED_ACCOUNT_NO);
        assertThat(testAccountUser.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testAccountUser.getLimit()).isEqualTo(UPDATED_LIMIT);
        assertThat(testAccountUser.isIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testAccountUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountUserMockMvc.perform(put("/api/account-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountUserDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountUser() throws Exception {
        // Initialize the database
        accountUserRepository.saveAndFlush(accountUser);
        int databaseSizeBeforeDelete = accountUserRepository.findAll().size();

        // Get the accountUser
        restAccountUserMockMvc.perform(delete("/api/account-users/{id}", accountUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountUser.class);
        AccountUser accountUser1 = new AccountUser();
        accountUser1.setId(1L);
        AccountUser accountUser2 = new AccountUser();
        accountUser2.setId(accountUser1.getId());
        assertThat(accountUser1).isEqualTo(accountUser2);
        accountUser2.setId(2L);
        assertThat(accountUser1).isNotEqualTo(accountUser2);
        accountUser1.setId(null);
        assertThat(accountUser1).isNotEqualTo(accountUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountUserDTO.class);
        AccountUserDTO accountUserDTO1 = new AccountUserDTO();
        accountUserDTO1.setId(1L);
        AccountUserDTO accountUserDTO2 = new AccountUserDTO();
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
        accountUserDTO2.setId(accountUserDTO1.getId());
        assertThat(accountUserDTO1).isEqualTo(accountUserDTO2);
        accountUserDTO2.setId(2L);
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
        accountUserDTO1.setId(null);
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accountUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accountUserMapper.fromId(null)).isNull();
    }
}
