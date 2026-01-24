package com.pm.patientservice.exception;

/**
 * @author onkardangi
 * @date 1/23/26
 * @time 09:39
 */
public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
