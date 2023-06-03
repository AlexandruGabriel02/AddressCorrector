package ro.uaic.info.AddressCorrector.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ro.uaic.info.AddressCorrector.models.Address;
import ro.uaic.info.AddressCorrector.models.CorrectedAddress;
import ro.uaic.info.AddressCorrector.services.AddressService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Log4j2
public class AddressController {
    private final AddressService addressService;
    @GetMapping(path = "addresses")
    @Operation(tags = "Corrector", description = "Obtains the list of corrected addresses")
    public List<CorrectedAddress> getCorrectedAddress(@RequestParam String country, @RequestParam String state, @RequestParam String city) {
        return addressService.getCorrectedAddress(new Address(country, state, city));
    }
}
