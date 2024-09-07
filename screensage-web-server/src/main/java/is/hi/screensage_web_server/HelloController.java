package is.hi.screensage_web_server;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {
    @GetMapping("/hello")
    public String HelloWorld() {
        return "Hello world";
    }
    
}
