package ar.edu.utn.frc.tup.lciii.service;


import ar.edu.utn.frc.tup.lciii.dtos.PostCountryRequest;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryGetDto;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        private final ModelMapper modelMapper;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .code((String) countryData.get("cca3"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .borders((List<String>) countryData.get("borders"))
                        .build();
        }


        private CountryGetDto mapToDTO(Country country) {
                return new CountryGetDto(country.getCode(), country.getName());
        }


        public List<CountryGetDto> getAllCountriesDto(String name, String code) {
                List<Country> countries = this.getAllCountries();
                List<CountryGetDto> rta= new ArrayList<>();
                List<CountryGetDto> countryGetDtos = new ArrayList<>();
                countries.forEach(x->{
                        CountryGetDto countryGetDto= new CountryGetDto(x.getCode(),x.getName());
                        countryGetDtos.add(countryGetDto);
                });
                if (code!=null){
                        countryGetDtos.stream()
                                .filter(x-> x.getCode().equals(code))
                                .forEach(x->{
                                        CountryGetDto countryGetDto= new CountryGetDto(x.getCode(),x.getName());
                                        rta.add(countryGetDto);
                                });
                }
                else if (name!=null){
                        countryGetDtos.stream()
                                .filter(x-> x.getName().equals(name))
                                .forEach(x->{
                                    CountryGetDto countryGetDto= new CountryGetDto(x.getCode(),x.getName());
                                        rta.add(countryGetDto);
                                });
                }
                else {
                    return countryGetDtos;
                }
                return rta;

        }


    public List<CountryGetDto> getAllCountriesDtoForContinent(String continent) {
        List<Country> countries = this.getAllCountries();
        List<CountryGetDto> rta= new ArrayList<>();

        countries.stream()
                .filter(x-> x.getRegion().equals(continent))
                .forEach(x->{
                    CountryGetDto countryGetDto= new CountryGetDto(x.getCode(),x.getName());
                    rta.add(countryGetDto);
                });
        return rta;
    }

    public List<CountryGetDto> getAllCountriesDtoForLanguage(String language) {
            List<Country> countries = this.getAllCountries();
        List<CountryGetDto> rta= new ArrayList<>();
        countries.forEach(x->{
            if (x.getLanguages()!=null){
                if (x.getLanguages().containsValue(language)){
                    CountryGetDto countryGetDto= new CountryGetDto(x.getCode(),x.getName());
                    rta.add(countryGetDto);

                };

            };

        });
        return rta;
    }

    public CountryGetDto getCountryMostBorders() {
            List<Country> countries = this.getAllCountries();
            CountryGetDto rta = new CountryGetDto();
        final int[] maxBorders = {0};
        if (countries.get(0).getBorders()!=null) {
            maxBorders[0] = countries.get(0).getBorders().size();
            rta.setCode(countries.get(0).getCode());
            rta.setName(countries.get(0).getName());
        }
            countries.forEach(x->{
                if (x.getBorders()!=null){
                    if (x.getBorders().size()> maxBorders[0]){
                        maxBorders[0] =x.getBorders().size();
                        rta.setCode(x.getCode());
                        rta.setName(x.getName());
                    }
                };
            });
        return rta;
    }

    public List<CountryGetDto> postCountry(PostCountryRequest postCountryRequest) {
        List<Country> countries = this.getAllCountries();
        List<Country> countriesToSave= new ArrayList<>();
        Collections.shuffle(countries);
        //no debe ser mayor a 10 revisar
        for (int i = 0; i < postCountryRequest.getAmountOfCountryToSave(); i++) {
            Country countryToSave = countries.get(i);
            countriesToSave.add(countryToSave);
            CountryEntity countryEntity = modelMapper.map(countryToSave,CountryEntity.class);
            countryRepository.save(countryEntity);
        }


        return countriesToSave.stream().map(this::mapToDTO).collect(Collectors.toList());

    }

}