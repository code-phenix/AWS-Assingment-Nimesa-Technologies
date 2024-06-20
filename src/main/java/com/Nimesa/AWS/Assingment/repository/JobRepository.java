package com.Nimesa.AWS.Assingment.repository;


import com.Nimesa.AWS.Assingment.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JobRepository extends JpaRepository<Job, Long> {
}