package com.ushine.versionupdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class VersionApplication {

    public static void main(String[] args) {

        SpringApplication.run(VersionApplication.class, args);

    }

}
