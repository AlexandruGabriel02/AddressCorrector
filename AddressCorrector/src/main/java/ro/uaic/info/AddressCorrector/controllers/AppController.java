package ro.uaic.info.AddressCorrector.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.uaic.info.AddressCorrector.database.MultimapDatabase;
import ro.uaic.info.AddressCorrector.database.Node;
import ro.uaic.info.AddressCorrector.models.Address;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Log4j2
public class AppController {
    private final MultimapDatabase multimapDatabase;

    @GetMapping(path = "addresses")
    public Address getCorrectedAddress(@RequestBody Address addressInput) {
        List<Node> nodes = multimapDatabase.get("România");
        Node node = nodes.get(0);

        log.info(node.getDefaultEntityName());
        return addressInput;
    }
}
