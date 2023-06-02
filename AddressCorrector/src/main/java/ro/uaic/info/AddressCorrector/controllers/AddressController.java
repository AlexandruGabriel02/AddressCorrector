package ro.uaic.info.AddressCorrector.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<CorrectedAddress> getCorrectedAddress(@RequestBody Address addressInput) {
        return addressService.getCorrectedAddress(addressInput);
    }
}
