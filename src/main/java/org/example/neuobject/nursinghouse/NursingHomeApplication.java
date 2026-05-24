package org.example.neuobject.nursinghouse.util;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p></p>
 *
 * @author li253
 * @version 1.0
 * @date 2026/5/24 17:39
 */
@SpringBootApplication
@MapperScan("org.example.neuobject.nursinghouse.dao")
public class NursingHomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NursingHomeApplication.class, args);
    }
}
