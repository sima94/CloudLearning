package com.cloudlearning.cloud.repositories.members;

import com.cloudlearning.cloud.models.members.Professor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends MemberBaseRepository<Professor, Long> {

}
