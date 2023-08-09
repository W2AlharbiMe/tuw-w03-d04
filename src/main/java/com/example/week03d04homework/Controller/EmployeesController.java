package com.example.week03d04homework.Controller;

import com.example.week03d04homework.ApiResponse.ApiResponse;
import com.example.week03d04homework.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeesController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> postEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse(message)));
        }

        // make sure that there's no employee with the same ID
        try {
            findEmployeeById(employee.getId());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse("the ID must be unique.")));
        } catch(Exception _e1) {
            // suppress
        }

        try {
            // validate (employment year) it must be a valid 4 digits.
            // please note that I have tried annotation:
            // @Digit(integer = 4, fraction = 0, message = "the field employment year must be 4 digits.")
            // however it did not work, it seems like it can only work on strings
            // this is another check that can be done via @Min and @Max
            // however I decided to use if as a change
            validateYear(employee.getEmploymentYear());
        } catch (Exception e2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse(e2.getMessage())));
        }


        // make sure that position is in lowercase and not something like SuPerVisOr or CoOrDiNatOr
        employee.setPosition(employee.getPosition().toLowerCase());

        employees.add(employee);

        return ResponseEntity.ok((new ApiResponse("Employee: " + employee.getName() + " have been created.")));
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors) {
        try {
            if(errors.hasErrors()) {
                String message = errors.getFieldError().getDefaultMessage();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse(message)));
            }

            try {
                Employee saved_employee = findEmployeeById(id);

                // don't update the ID.
                saved_employee.setName(employee.getName());
                saved_employee.setAge(employee.getAge());
                saved_employee.setPosition(employee.getPosition().toLowerCase());

                try {
                    // validate (employment year) it must be a valid 4 digits.
                    // please note that I have tried annotation:
                    // @Digit(integer = 4, fraction = 0, message = "the field employment year must be 4 digits.")
                    // however it did not work, it seems like it can only work on strings
                    // this is another check that can be done via @Min and @Max
                    // however I decided to use if as a change
                    validateYear(employee.getEmploymentYear());
                } catch (Exception e1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse(e1.getMessage())));
                }

                // update year after validation
                saved_employee.setEmploymentYear(employee.getEmploymentYear());

                return ResponseEntity.ok((new ApiResponse("Employee " + employee.getName() + " have been updated.")));
            } catch (Exception e2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse(e2.getMessage())));
            }

        } catch (Exception e1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiResponse(e1.getMessage())));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id) {
        try {
            for (int i = 0; i < employees.size(); i++) {
                Employee e = employees.get(i);
                if(e.getId().equalsIgnoreCase(id)) {
                    employees.remove(i);
                    return ResponseEntity.ok((new ApiResponse("Employee with ID: " + id + " have been deleted.")));
                }
            }

            return ResponseEntity.ok((new ApiResponse("Employee with ID: " + id + " not found.")));
        } catch (Exception e3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiResponse(e3.getMessage())));
        }
    }

    @PutMapping("/{id}/set-on-leave")
    public ResponseEntity<ApiResponse> setEmployeeOnLeave(@PathVariable String id) {
        try {
            Employee saved_employee = findEmployeeById(id);

            if(saved_employee.isOnLeave()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiResponse("the employee: " + saved_employee.getName() + " is already on leave.")));
            }

            saved_employee.setOnLeave(true);
            saved_employee.setAnnualLeave(saved_employee.getAnnualLeave() - 1);

            return ResponseEntity.ok((new ApiResponse("the employee: " + saved_employee.getName() + " have been set to leave.")));
        } catch(Exception e4) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiResponse(e4.getMessage())));
        }
    }


    // this is a simple method that it will get id and return employee if it is in the arrayList
    // or throw exception in case no employee with the requested id
    // for readability this will be used in multiple places:
    // updateEmployee, postEmployee, setEmployeeOnLeave
    private Employee findEmployeeById(String id) throws Exception {
        for (Employee e: employees) {
            if(e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }

        throw new Exception("Employee not found.");
    }


    // this method validate the year and make sure it is 4 digits.
    // this validation is used in postEmployee, updateEmployee
    private void validateYear(int year) throws Exception {
        if(year < 1900 || year > 2500) {
            throw new Exception("the field employment year must be 4 digits and from year 1900 to 2500.");
        }
    }


}
