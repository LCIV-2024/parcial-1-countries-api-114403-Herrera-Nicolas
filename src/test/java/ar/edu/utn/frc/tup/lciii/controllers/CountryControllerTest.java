package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.PostCountryRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryGetDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @SpyBean
    private MockMvc mockMvc;
    @MockBean
    private CountryService countryService;
    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllCountries() throws Exception {
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));

        when(countryService.getAllCountriesDto(null,null)).thenReturn(countryList);
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }
    @Test
    void getAllCountriesByName() throws Exception {
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));

        when(countryService.getAllCountriesDto("Argentina",null)).thenReturn(countryList);
        mockMvc.perform(get("/api/countries?name=Argentina"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }
    @Test
    void getAllCountriesByCode() throws Exception {
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));

        when(countryService.getAllCountriesDto(null,"ARG")).thenReturn(countryList);
        mockMvc.perform(get("/api/countries?code=ARG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }
    @Test
    void postCountry() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));
        PostCountryRequest postCountryRequest = new PostCountryRequest(1);
        when(countryService.postCountry(postCountryRequest)).thenReturn(countryList);
        String json = objectMapper.writeValueAsString(postCountryRequest);
        mockMvc.perform(post("/api/countries")
               .contentType("application/json")
               .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void getAllCountriesByContinent() throws Exception {
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));

        when(countryService.getAllCountriesDtoForContinent("Americas")).thenReturn(countryList);

        mockMvc.perform(get("/api/countries/Americas/continent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void getCountriesByLanguage() throws Exception {
        List<CountryGetDto> countryList = new ArrayList<>();
        countryList.add(new CountryGetDto("ARG","Argentina"));

        when(countryService.getAllCountriesDtoForLanguage("Spanish")).thenReturn(countryList);

        mockMvc.perform(get("/api/countries/Spanish/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void getCountryMostBorders() throws Exception {

        CountryGetDto country = new CountryGetDto("ARG","Argentina");

        when(countryService.getCountryMostBorders()).thenReturn(country);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("ARG"))
                .andExpect(jsonPath("name").value("Argentina"));

    }
}