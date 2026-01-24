package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient) {
        return PatientResponseDTO.builder().id(patient.getId().toString()).name(patient.getName())
                .address(patient.getAddress()).dateOfBirth(patient.getDateOfBirth().toString())
                .email(patient.getEmail()).build();

    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO) {
        return Patient.builder().name(patientRequestDTO.getName())
                .address(patientRequestDTO.getAddress()).email(patientRequestDTO.getEmail())
                .dateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate())).build();
    }
}
