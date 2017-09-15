package com.kiffcode.banking.web.rest;

import com.kiffcode.banking.OnlineBankingApp;

import com.kiffcode.banking.domain.BillPayment;
import com.kiffcode.banking.domain.Vendor;
import com.kiffcode.banking.domain.AccountUser;
import com.kiffcode.banking.repository.BillPaymentRepository;
import com.kiffcode.banking.service.BillPaymentService;
import com.kiffcode.banking.service.dto.BillPaymentDTO;
import com.kiffcode.banking.service.mapper.BillPaymentMapper;
import com.kiffcode.banking.web.rest.errors.ExceptionTranslator;
import com.kiffcode.banking.service.dto.BillPaymentCriteria;
import com.kiffcode.banking.service.BillPaymentQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BillPaymentResource REST controller.
 *
 * @see BillPaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlineBankingApp.class)
public class BillPaymentResourceIntTest {

    private static final String DEFAULT_SCHEDULED_DATE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY_MODE = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY_MODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYMENT_COMPLETE = false;
    private static final Boolean UPDATED_PAYMENT_COMPLETE = true;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private BillPaymentMapper billPaymentMapper;

    @Autowired
    private BillPaymentService billPaymentService;

    @Autowired
    private BillPaymentQueryService billPaymentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBillPaymentMockMvc;

    private BillPayment billPayment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillPaymentResource billPaymentResource = new BillPaymentResource(billPaymentService, billPaymentQueryService);
        this.restBillPaymentMockMvc = MockMvcBuilders.standaloneSetup(billPaymentResource)
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
    public static BillPayment createEntity(EntityManager em) {
        BillPayment billPayment = new BillPayment()
            .scheduledDate(DEFAULT_SCHEDULED_DATE)
            .frequencyMode(DEFAULT_FREQUENCY_MODE)
            .paymentComplete(DEFAULT_PAYMENT_COMPLETE)
            .amount(DEFAULT_AMOUNT);
        // Add required entity
        Vendor vendor = VendorResourceIntTest.createEntity(em);
        em.persist(vendor);
        em.flush();
        billPayment.setVendor(vendor);
        // Add required entity
        AccountUser accountUser = AccountUserResourceIntTest.createEntity(em);
        em.persist(accountUser);
        em.flush();
        billPayment.setAccountUser(accountUser);
        return billPayment;
    }

    @Before
    public void initTest() {
        billPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillPayment() throws Exception {
        int databaseSizeBeforeCreate = billPaymentRepository.findAll().size();

        // Create the BillPayment
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(billPayment);
        restBillPaymentMockMvc.perform(post("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getScheduledDate()).isEqualTo(DEFAULT_SCHEDULED_DATE);
        assertThat(testBillPayment.getFrequencyMode()).isEqualTo(DEFAULT_FREQUENCY_MODE);
        assertThat(testBillPayment.isPaymentComplete()).isEqualTo(DEFAULT_PAYMENT_COMPLETE);
        assertThat(testBillPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBillPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billPaymentRepository.findAll().size();

        // Create the BillPayment with an existing ID
        billPayment.setId(1L);
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(billPayment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillPaymentMockMvc.perform(post("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPaymentCompleteIsRequired() throws Exception {
        int databaseSizeBeforeTest = billPaymentRepository.findAll().size();
        // set the field null
        billPayment.setPaymentComplete(null);

        // Create the BillPayment, which fails.
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(billPayment);

        restBillPaymentMockMvc.perform(post("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isBadRequest());

        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = billPaymentRepository.findAll().size();
        // set the field null
        billPayment.setAmount(null);

        // Create the BillPayment, which fails.
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(billPayment);

        restBillPaymentMockMvc.perform(post("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isBadRequest());

        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillPayments() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList
        restBillPaymentMockMvc.perform(get("/api/bill-payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].frequencyMode").value(hasItem(DEFAULT_FREQUENCY_MODE.toString())))
            .andExpect(jsonPath("$.[*].paymentComplete").value(hasItem(DEFAULT_PAYMENT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get the billPayment
        restBillPaymentMockMvc.perform(get("/api/bill-payments/{id}", billPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billPayment.getId().intValue()))
            .andExpect(jsonPath("$.scheduledDate").value(DEFAULT_SCHEDULED_DATE.toString()))
            .andExpect(jsonPath("$.frequencyMode").value(DEFAULT_FREQUENCY_MODE.toString()))
            .andExpect(jsonPath("$.paymentComplete").value(DEFAULT_PAYMENT_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByScheduledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where scheduledDate equals to DEFAULT_SCHEDULED_DATE
        defaultBillPaymentShouldBeFound("scheduledDate.equals=" + DEFAULT_SCHEDULED_DATE);

        // Get all the billPaymentList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultBillPaymentShouldNotBeFound("scheduledDate.equals=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByScheduledDateIsInShouldWork() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where scheduledDate in DEFAULT_SCHEDULED_DATE or UPDATED_SCHEDULED_DATE
        defaultBillPaymentShouldBeFound("scheduledDate.in=" + DEFAULT_SCHEDULED_DATE + "," + UPDATED_SCHEDULED_DATE);

        // Get all the billPaymentList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultBillPaymentShouldNotBeFound("scheduledDate.in=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByScheduledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where scheduledDate is not null
        defaultBillPaymentShouldBeFound("scheduledDate.specified=true");

        // Get all the billPaymentList where scheduledDate is null
        defaultBillPaymentShouldNotBeFound("scheduledDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByFrequencyModeIsEqualToSomething() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where frequencyMode equals to DEFAULT_FREQUENCY_MODE
        defaultBillPaymentShouldBeFound("frequencyMode.equals=" + DEFAULT_FREQUENCY_MODE);

        // Get all the billPaymentList where frequencyMode equals to UPDATED_FREQUENCY_MODE
        defaultBillPaymentShouldNotBeFound("frequencyMode.equals=" + UPDATED_FREQUENCY_MODE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByFrequencyModeIsInShouldWork() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where frequencyMode in DEFAULT_FREQUENCY_MODE or UPDATED_FREQUENCY_MODE
        defaultBillPaymentShouldBeFound("frequencyMode.in=" + DEFAULT_FREQUENCY_MODE + "," + UPDATED_FREQUENCY_MODE);

        // Get all the billPaymentList where frequencyMode equals to UPDATED_FREQUENCY_MODE
        defaultBillPaymentShouldNotBeFound("frequencyMode.in=" + UPDATED_FREQUENCY_MODE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByFrequencyModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where frequencyMode is not null
        defaultBillPaymentShouldBeFound("frequencyMode.specified=true");

        // Get all the billPaymentList where frequencyMode is null
        defaultBillPaymentShouldNotBeFound("frequencyMode.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByPaymentCompleteIsEqualToSomething() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where paymentComplete equals to DEFAULT_PAYMENT_COMPLETE
        defaultBillPaymentShouldBeFound("paymentComplete.equals=" + DEFAULT_PAYMENT_COMPLETE);

        // Get all the billPaymentList where paymentComplete equals to UPDATED_PAYMENT_COMPLETE
        defaultBillPaymentShouldNotBeFound("paymentComplete.equals=" + UPDATED_PAYMENT_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByPaymentCompleteIsInShouldWork() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where paymentComplete in DEFAULT_PAYMENT_COMPLETE or UPDATED_PAYMENT_COMPLETE
        defaultBillPaymentShouldBeFound("paymentComplete.in=" + DEFAULT_PAYMENT_COMPLETE + "," + UPDATED_PAYMENT_COMPLETE);

        // Get all the billPaymentList where paymentComplete equals to UPDATED_PAYMENT_COMPLETE
        defaultBillPaymentShouldNotBeFound("paymentComplete.in=" + UPDATED_PAYMENT_COMPLETE);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByPaymentCompleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where paymentComplete is not null
        defaultBillPaymentShouldBeFound("paymentComplete.specified=true");

        // Get all the billPaymentList where paymentComplete is null
        defaultBillPaymentShouldNotBeFound("paymentComplete.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where amount equals to DEFAULT_AMOUNT
        defaultBillPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the billPaymentList where amount equals to UPDATED_AMOUNT
        defaultBillPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultBillPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the billPaymentList where amount equals to UPDATED_AMOUNT
        defaultBillPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBillPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);

        // Get all the billPaymentList where amount is not null
        defaultBillPaymentShouldBeFound("amount.specified=true");

        // Get all the billPaymentList where amount is null
        defaultBillPaymentShouldNotBeFound("amount.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBillPaymentShouldBeFound(String filter) throws Exception {
        restBillPaymentMockMvc.perform(get("/api/bill-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].frequencyMode").value(hasItem(DEFAULT_FREQUENCY_MODE.toString())))
            .andExpect(jsonPath("$.[*].paymentComplete").value(hasItem(DEFAULT_PAYMENT_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBillPaymentShouldNotBeFound(String filter) throws Exception {
        restBillPaymentMockMvc.perform(get("/api/bill-payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBillPayment() throws Exception {
        // Get the billPayment
        restBillPaymentMockMvc.perform(get("/api/bill-payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();

        // Update the billPayment
        BillPayment updatedBillPayment = billPaymentRepository.findOne(billPayment.getId());
        updatedBillPayment
            .scheduledDate(UPDATED_SCHEDULED_DATE)
            .frequencyMode(UPDATED_FREQUENCY_MODE)
            .paymentComplete(UPDATED_PAYMENT_COMPLETE)
            .amount(UPDATED_AMOUNT);
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(updatedBillPayment);

        restBillPaymentMockMvc.perform(put("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isOk());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate);
        BillPayment testBillPayment = billPaymentList.get(billPaymentList.size() - 1);
        assertThat(testBillPayment.getScheduledDate()).isEqualTo(UPDATED_SCHEDULED_DATE);
        assertThat(testBillPayment.getFrequencyMode()).isEqualTo(UPDATED_FREQUENCY_MODE);
        assertThat(testBillPayment.isPaymentComplete()).isEqualTo(UPDATED_PAYMENT_COMPLETE);
        assertThat(testBillPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingBillPayment() throws Exception {
        int databaseSizeBeforeUpdate = billPaymentRepository.findAll().size();

        // Create the BillPayment
        BillPaymentDTO billPaymentDTO = billPaymentMapper.toDto(billPayment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBillPaymentMockMvc.perform(put("/api/bill-payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billPaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the BillPayment in the database
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBillPayment() throws Exception {
        // Initialize the database
        billPaymentRepository.saveAndFlush(billPayment);
        int databaseSizeBeforeDelete = billPaymentRepository.findAll().size();

        // Get the billPayment
        restBillPaymentMockMvc.perform(delete("/api/bill-payments/{id}", billPayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BillPayment> billPaymentList = billPaymentRepository.findAll();
        assertThat(billPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillPayment.class);
        BillPayment billPayment1 = new BillPayment();
        billPayment1.setId(1L);
        BillPayment billPayment2 = new BillPayment();
        billPayment2.setId(billPayment1.getId());
        assertThat(billPayment1).isEqualTo(billPayment2);
        billPayment2.setId(2L);
        assertThat(billPayment1).isNotEqualTo(billPayment2);
        billPayment1.setId(null);
        assertThat(billPayment1).isNotEqualTo(billPayment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillPaymentDTO.class);
        BillPaymentDTO billPaymentDTO1 = new BillPaymentDTO();
        billPaymentDTO1.setId(1L);
        BillPaymentDTO billPaymentDTO2 = new BillPaymentDTO();
        assertThat(billPaymentDTO1).isNotEqualTo(billPaymentDTO2);
        billPaymentDTO2.setId(billPaymentDTO1.getId());
        assertThat(billPaymentDTO1).isEqualTo(billPaymentDTO2);
        billPaymentDTO2.setId(2L);
        assertThat(billPaymentDTO1).isNotEqualTo(billPaymentDTO2);
        billPaymentDTO1.setId(null);
        assertThat(billPaymentDTO1).isNotEqualTo(billPaymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(billPaymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(billPaymentMapper.fromId(null)).isNull();
    }
}
