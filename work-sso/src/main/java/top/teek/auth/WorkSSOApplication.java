package top.teek.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Teeker
 * @date 2023/9/24 3:10
 * @note
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WorkSSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkSSOApplication.class, args);
    }
}
