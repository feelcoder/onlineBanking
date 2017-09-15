package com.kiffcode.banking.web.rest;

import com.kiffcode.banking.OnlineBankingApp;

import com.kiffcode.banking.domain.Vendor;
import com.kiffcode.banking.repository.VendorRepository;
import com.kiffcode.banking.service.VendorService;
import com.kiffcode.banking.service.dto.VendorDTO;
import com.kiffcode.banking.service.mapper.VendorMapper;
import com.kiffcode.banking.web.rest.errors.ExceptionTranslator;
import com.kiffcode.banking.service.dto.VendorCriteria;
import com.kiffcode.banking.service.VendorQueryService;

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
 * Test class for the VendorResource REST controller.
 *
 * @see VendorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlineBankingApp.class)
public class VendorResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REGISTERED = false;
    private static final Boolean UPDATED_IS_REGISTERED = true;

    private static final String DEFAULT_EXPIRING_DATE = "AAAAAAAAAA";
    private static final String UPDATED_EXPIRING_DATE = "BBBBBBBBBB";

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorQueryService vendorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVendorMockMvc;

    private Vendor vendor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VendorResource vendorResource = new VendorResource(vendorService, vendorQueryService);
        this.restVendorMockMvc = MockMvcBuilders.standaloneSetup(vendorResource)
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
    public static Vendor createEntity(EntityManager em) {
        Vendor vendor = new Vendor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .companyName(DEFAULT_COMPANY_NAME)
            .isRegistered(DEFAULT_IS_REGISTERED)
            .expiringDate(DEFAULT_EXPIRING_DATE);
        return vendor;
    }

    @Before
    public void initTest() {
        vendor = createEntity(em);
    }

    @Test
    @Transactional
    public void createVendor() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);
        restVendorMockMvc.perform(post("/api/vendors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isCreated());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVendor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testVendor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVendor.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testVendor.isIsRegistered()).isEqualTo(DEFAULT_IS_REGISTERED);
        assertThat(testVendor.getExpiringDate()).isEqualTo(DEFAULT_EXPIRING_DATE);
    }

    @Test
    @Transactional
    public void createVendorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // Create the Vendor with an existing ID
        vendor.setId(1L);
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorMockMvc.perform(post("/api/vendors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsRegisteredIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setIsRegistered(null);

        // Create the Vendor, which fails.
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        restVendorMockMvc.perform(post("/api/vendors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVendors() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList
        restVendorMockMvc.perform(get("/api/vendors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].isRegistered").value(hasItem(DEFAULT_IS_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].expiringDate").value(hasItem(DEFAULT_EXPIRING_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        restVendorMockMvc.perform(get("/api/vendors/{id}", vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vendor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.isRegistered").value(DEFAULT_IS_REGISTERED.booleanValue()))
            .andExpect(jsonPath("$.expiringDate").value(DEFAULT_EXPIRING_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllVendorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where firstName equals to DEFAULT_FIRST_NAME
        defaultVendorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the vendorList where firstName equals to UPDATED_FIRST_NAME
        defaultVendorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultVendorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the vendorList where firstName equals to UPDATED_FIRST_NAME
        defaultVendorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where firstName is not null
        defaultVendorShouldBeFound("firstName.specified=true");

        // Get all the vendorList where firstName is null
        defaultVendorShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllVendorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where lastName equals to DEFAULT_LAST_NAME
        defaultVendorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the vendorList where lastName equals to UPDATED_LAST_NAME
        defaultVendorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultVendorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the vendorList where lastName equals to UPDATED_LAST_NAME
        defaultVendorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where lastName is not null
        defaultVendorShouldBeFound("lastName.specified=true");

        // Get all the vendorList where lastName is null
        defaultVendorShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllVendorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where email equals to DEFAULT_EMAIL
        defaultVendorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the vendorList where email equals to UPDATED_EMAIL
        defaultVendorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllVendorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultVendorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the vendorList where email equals to UPDATED_EMAIL
        defaultVendorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllVendorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where email is not null
        defaultVendorShouldBeFound("email.specified=true");

        // Get all the vendorList where email is null
        defaultVendorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllVendorsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where companyName equals to DEFAULT_COMPANY_NAME
        defaultVendorShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the vendorList where companyName equals to UPDATED_COMPANY_NAME
        defaultVendorShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultVendorShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the vendorList where companyName equals to UPDATED_COMPANY_NAME
        defaultVendorShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllVendorsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where companyName is not null
        defaultVendorShouldBeFound("companyName.specified=true");

        // Get all the vendorList where companyName is null
        defaultVendorShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllVendorsByIsRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where isRegistered equals to DEFAULT_IS_REGISTERED
        defaultVendorShouldBeFound("isRegistered.equals=" + DEFAULT_IS_REGISTERED);

        // Get all the vendorList where isRegistered equals to UPDATED_IS_REGISTERED
        defaultVendorShouldNotBeFound("isRegistered.equals=" + UPDATED_IS_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllVendorsByIsRegisteredIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where isRegistered in DEFAULT_IS_REGISTERED or UPDATED_IS_REGISTERED
        defaultVendorShouldBeFound("isRegistered.in=" + DEFAULT_IS_REGISTERED + "," + UPDATED_IS_REGISTERED);

        // Get all the vendorList where isRegistered equals to UPDATED_IS_REGISTERED
        defaultVendorShouldNotBeFound("isRegistered.in=" + UPDATED_IS_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllVendorsByIsRegisteredIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where isRegistered is not null
        defaultVendorShouldBeFound("isRegistered.specified=true");

        // Get all the vendorList where isRegistered is null
        defaultVendorShouldNotBeFound("isRegistered.specified=false");
    }

    @Test
    @Transactional
    public void getAllVendorsByExpiringDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where expiringDate equals to DEFAULT_EXPIRING_DATE
        defaultVendorShouldBeFound("expiringDate.equals=" + DEFAULT_EXPIRING_DATE);

        // Get all the vendorList where expiringDate equals to UPDATED_EXPIRING_DATE
        defaultVendorShouldNotBeFound("expiringDate.equals=" + UPDATED_EXPIRING_DATE);
    }

    @Test
    @Transactional
    public void getAllVendorsByExpiringDateIsInShouldWork() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where expiringDate in DEFAULT_EXPIRING_DATE or UPDATED_EXPIRING_DATE
        defaultVendorShouldBeFound("expiringDate.in=" + DEFAULT_EXPIRING_DATE + "," + UPDATED_EXPIRING_DATE);

        // Get all the vendorList where expiringDate equals to UPDATED_EXPIRING_DATE
        defaultVendorShouldNotBeFound("expiringDate.in=" + UPDATED_EXPIRING_DATE);
    }

    @Test
    @Transactional
    public void getAllVendorsByExpiringDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList where expiringDate is not null
        defaultVendorShouldBeFound("expiringDate.specified=true");

        // Get all the vendorList where expiringDate is null
        defaultVendorShouldNotBeFound("expiringDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVendorShouldBeFound(String filter) throws Exception {
        restVendorMockMvc.perform(get("/api/vendors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].isRegistered").value(hasItem(DEFAULT_IS_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].expiringDate").value(hasItem(DEFAULT_EXPIRING_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVendorShouldNotBeFound(String filter) throws Exception {
        restVendorMockMvc.perform(get("/api/vendors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVendor() throws Exception {
        // Get the vendor
        restVendorMockMvc.perform(get("/api/vendors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor
        Vendor updatedVendor = vendorRepository.findOne(vendor.getId());
        updatedVendor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .companyName(UPDATED_COMPANY_NAME)
            .isRegistered(UPDATED_IS_REGISTERED)
            .expiringDate(UPDATED_EXPIRING_DATE);
        VendorDTO vendorDTO = vendorMapper.toDto(updatedVendor);

        restVendorMockMvc.perform(put("/api/vendors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVendor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testVendor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVendor.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testVendor.isIsRegistered()).isEqualTo(UPDATED_IS_REGISTERED);
        assertThat(testVendor.getExpiringDate()).isEqualTo(UPDATED_EXPIRING_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVendorMockMvc.perform(put("/api/vendors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorDTO)))
            .andExpect(status().isCreated());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);
        int databaseSizeBeforeDelete = vendorRepository.findAll().size();

        // Get the vendor
        restVendorMockMvc.perform(delete("/api/vendors/{id}", vendor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        Vendor vendor2 = new Vendor();
        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);
        vendor2.setId(2L);
        assertThat(vendor1).isNotEqualTo(vendor2);
        vendor1.setId(null);
        assertThat(vendor1).isNotEqualTo(vendor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendorDTO.class);
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setId(1L);
        VendorDTO vendorDTO2 = new VendorDTO();
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
        vendorDTO2.setId(vendorDTO1.getId());
        assertThat(vendorDTO1).isEqualTo(vendorDTO2);
        vendorDTO2.setId(2L);
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
        vendorDTO1.setId(null);
        assertThat(vendorDTO1).isNotEqualTo(vendorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vendorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vendorMapper.fromId(null)).isNull();
    }
}
