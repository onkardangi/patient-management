package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    /**
     * @param patientRepository
     * @param billingServiceGrpcClient
     */
    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    /**
     * @return
     */
    public List<PatientResponseDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    /**
     * @param patientRequestDTO
     * @return
     */
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + "already exists " + patientRequestDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
        return PatientMapper.toDTO(patient);
    }

    /**
     * @param id
     * @param patientRequestDTO
     * @return
     */
    public PatientResponseDTO updatePatient(UUID id, @Valid PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        String newEmail = patientRequestDTO.getEmail();
        if (patientRepository.existsByEmailAndIdNot(newEmail, id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email already exists: " + newEmail
            );
        }

        patient = patient.toBuilder().name(patientRequestDTO.getName()).address(patientRequestDTO.getAddress())
                .email(patientRequestDTO.getEmail()).dateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()))
                .build();

        patientRepository.save(patient);
        return PatientMapper.toDTO(patient);

    }

    /**
     * We should consider it to act as a soft delete using status instead of deleting it
     * @param id
     */
    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        patientRepository.delete(patient);
        PatientMapper.toDTO(patient);
    }
}
