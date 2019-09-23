package com.cloudlearning.cloud.services.members.professor;

import com.cloudlearning.cloud.models.members.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessorService {

    Professor findById(Long id);

    Page<Professor> findAll(Pageable pageable);

    Professor create(Professor professor);
}
