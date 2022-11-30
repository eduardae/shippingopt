package com.shippingopt.packager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shippingopt.packager.TestUtils;
import com.shippingopt.packager.service.PackageSorterService;
import com.shippingopt.packager.service.external.CurrencyConverterApiService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PackageSorterController.class)
public class PackageSorterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PackageSorterService packageSorterService;

    // currency converter mocked to avoid test failures in case of API downstate
    @MockBean
    private CurrencyConverterApiService converterApiService;


    @Test
    public void packageOptPostTestOk() throws Exception {
        mockMvc.perform(post("/package-sort/")
                        .content(mapper.writeValueAsString(TestUtils.getOkPackageSpecs("EUR")))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void packageOptPostTestNoSuitable() throws Exception {
        mockMvc.perform(post("/package-sort/")
                        .content(mapper.writeValueAsString(TestUtils.getNoSuitablePackageSpecs()))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void packageOptPostTestInvalidReq() throws Exception {
        mockMvc.perform(post("/package-sort/")
                        .content(mapper.writeValueAsString(TestUtils.getInvalidPackageSpecs()))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        public PackageSorterService packageSorterService() {
            return new PackageSorterService();
        }


    }

}
