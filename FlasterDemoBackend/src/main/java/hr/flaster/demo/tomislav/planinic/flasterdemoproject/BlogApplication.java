package hr.flaster.demo.tomislav.planinic.flasterdemoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"hr.flaster.demo.tomislav.planinic.flasterdemoproject.config" , "hr.flaster.demo.tomislav.planinic.flasterdemoproject.security","hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller"})
@EntityScan(basePackages = {"hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity"})
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }


}
