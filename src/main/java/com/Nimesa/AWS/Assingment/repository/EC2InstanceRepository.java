package com.Nimesa.AWS.Assingment.repository;
import com.Nimesa.AWS.Assingment.model.EC2Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EC2InstanceRepository extends JpaRepository<EC2Instance, Long> {
    List<EC2Instance> findByJobId(Long jobId);
}