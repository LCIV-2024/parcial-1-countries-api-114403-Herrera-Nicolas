package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.PostCountryRequest;
import ar.edu.utn.frc.tup.lciii.model.CountryGetDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("api/countries")
    public ResponseEntity<List<CountryGetDto>> getAllCountries(@RequestParam(required = false) String name,@RequestParam(required = false) String code) {
        return ResponseEntity.ok(countryService.getAllCountriesDto(name,code));

    }
    @PostMapping("api/countries")
    public List<CountryGetDto> PostCountry(@RequestBody PostCountryRequest postCountryRequest) {
        return countryService.postCountry(postCountryRequest);

    }

    @GetMapping("api/countries/{continent}/continent")
    public List<CountryGetDto> getAllCountriesByContinent(@PathVariable String continent) {
        return countryService.getAllCountriesDtoForContinent(continent);

    }
    @GetMapping("api/countries/{language}/language")
    public List<CountryGetDto> getCountriesByLanguage(@PathVariable String language) {
        return countryService.getAllCountriesDtoForLanguage(language);

    }
    @GetMapping("api/countries/most-borders")
    public CountryGetDto getCountryMostBorders() {
        return countryService.getCountryMostBorders();

    }
}