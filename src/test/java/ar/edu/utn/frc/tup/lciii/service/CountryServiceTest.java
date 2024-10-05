package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.PostCountryRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryGetDto;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CountryServiceTest {
    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private RestTemplate restTemplate;
    @SpyBean
    private CountryService countryService;
    @BeforeEach
    void setUp() {

        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> countryData = new HashMap<>();
        countryData.put("name", Collections.singletonMap("common", "Test Country"));
        countryData.put("cca3", "TST");
        countryData.put("population", 1000000);
        countryData.put("area", 10000.0);
        countryData.put("region", "Test Region");
        countryData.put("languages", Collections.singletonMap("eng", "English"));
        countryData.put("borders", Arrays.asList("BRA", "ARG"));
        mockResponse.add(countryData);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(List.class)))
                .thenReturn(mockResponse);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void getAllCountries() {

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> countryData = new HashMap<>();
        countryData.put("name", Collections.singletonMap("common", "Test Country"));
        countryData.put("cca3", "TST");
        countryData.put("population", 1000000);
        countryData.put("area", 10000.0);
        countryData.put("region", "Test Region");
        countryData.put("languages", Collections.singletonMap("eng", "English"));
        countryData.put("borders", Arrays.asList("BRA", "ARG"));
        mockResponse.add(countryData);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(List.class)))
                .thenReturn(mockResponse);

        CountryService countryService = new CountryService(null, null, restTemplate);

        List<Country> countries = countryService.getAllCountries();

        assertEquals(1, countries.size());
        Country country = countries.get(0);
        assertEquals("Test Country", country.getName());
        assertEquals("TST", country.getCode());

    }

    @Test
    void getAllCountriesDto() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country("Argentina",10L,20,"ARG","Americas",null,null));
        when(countryService.getAllCountries()).thenReturn(countryList);

        List<CountryGetDto> result = countryService.getAllCountriesDto("Argentina",null);
        assertEquals(result.get(0).getName(),"Argentina");
        assertEquals(result.get(0).getCode(),"ARG");

    }

    @Test
    void getAllCountriesDtoForContinent() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country("Argentina",10L,20,"ARG","Americas",null,null));
        when(countryService.getAllCountries()).thenReturn(countryList);

        List<CountryGetDto> result = countryService.getAllCountriesDtoForContinent("Americas");
        assertEquals(result.get(0).getName(),"Argentina");
        assertEquals(result.get(0).getCode(),"ARG");
    }

    @Test
    void getAllCountriesDtoForLanguage() {
        List<Country> countryList = new ArrayList<>();
        Map<String,String> languages = new HashMap<>();
        languages.put("Es","Spanish");
        countryList.add(new Country("Argentina",10L,20,"ARG","Americas",null,languages));
        when(countryService.getAllCountries()).thenReturn(countryList);

        List<CountryGetDto> result = countryService.getAllCountriesDtoForLanguage("Spanish");
        assertEquals(result.get(0).getName(),"Argentina");
        assertEquals(result.get(0).getCode(),"ARG");
    }

    @Test
    void getCountryMostBorders() {
        List<Country> countryList = new ArrayList<>();
        List<String> bordesArg = new ArrayList<>();
        bordesArg.add("Peru");
        bordesArg.add("Paraguay");
        bordesArg.add("Chile");
        countryList.add(new Country("Argentina",10L,20,"ARG","Americas",bordesArg,null));

        List<String> bordesBol = new ArrayList<>();
        bordesArg.add("Peru");
        bordesArg.add("Paraguay");

        countryList.add(new Country("Bolivia",20L,15,"BOL","Americas",bordesBol,null));
        List<String> bordesCh = new ArrayList<>();
        bordesArg.add("Peru");

        countryList.add(new Country("Chile",30L,25,"CHL","Americas",bordesCh,null));
        when(countryService.getAllCountries()).thenReturn(countryList);

        CountryGetDto result = countryService.getCountryMostBorders();
        assertEquals(result.getName(),"Argentina");
        assertEquals(result.getCode(),"ARG");
    }

    @Test
    void postCountry() {
        List<Country> countryList = new ArrayList<>();
        List<String> bordesArg = new ArrayList<>();
        bordesArg.add("Peru");
        bordesArg.add("Paraguay");
        bordesArg.add("Chile");
        Map<String,String> languages = new HashMap<>();
        languages.put("Es","Spanish");
        countryList.add(new Country("Argentina",10L,20,"ARG","Americas",bordesArg,languages));
        when(countryService.getAllCountries()).thenReturn(countryList);

        PostCountryRequest postCountryRequest =new PostCountryRequest(1);
        List<CountryGetDto> result =  countryService.postCountry(postCountryRequest);
        assertEquals(result.get(0).getName(),"Argentina");
        assertEquals(result.get(0).getCode(),"ARG");

    }
}