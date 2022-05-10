package com.debijenkorf.elnazfaghihiimageServiceassignment;

import com.debijenkorf.elnazfaghihiimageServiceassignment.utils.DefaultProfileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class ElnazfaghihiImageServiceAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElnazfaghihiImageServiceAssignmentApplication.class, args);
        SpringApplication app = new SpringApplication(ElnazfaghihiImageServiceAssignmentApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
    }

}
