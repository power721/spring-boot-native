package cn.har01d.springbootnative;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public List<User> getUser() {
        return List.of(new User("test", 18));
    }
}
