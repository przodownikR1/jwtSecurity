package pl.java.scalatech.web;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
class TestController {
    private static final String SIMPLE = "/simple";
    private static final String SIMPLE_RESPONSE = "simple response";

    @GetMapping(SIMPLE)
    String simpleResponse() {
        return SIMPLE_RESPONSE;
}
}