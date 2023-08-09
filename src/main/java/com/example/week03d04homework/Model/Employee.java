package com.example.week03d04homework.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "the id field is required.")
    private String id;

    @NotEmpty(message = "the name field is required.")
    @Size(min = 5, message = "the name value must be more than 4 chars.")
    private String name;

    @NotNull(message = "the age field is required.")
    @Positive(message = "the age must be a positive number.")
    @Min(message = "the age value must be greater than 25.", value = 26)
    private int age;

    @NotEmpty(message = "the position field is required.")
    @Pattern(regexp = "(?i)\\b(supervisor)\\b?|(?i)\\b(coordinator)\\b", message = "the position can only be either a 'supervisor' or 'coordinator'.")
    private String position;

    @AssertFalse(message = "on leave must be false.")
    private boolean onLeave = false;

    @NotNull(message = "the employee year field is required.")
    @Positive(message = "the employment year must be a positive number.")
    private int employmentYear;

    @NotNull(message = "the annual leave field is required.")
    @Positive(message = "the annual leave must be a positive number.")
    private int annualLeave;
}
