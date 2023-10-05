package com.assemble.file.repository;

import com.assemble.file.entity.AttachedFile;
import com.assemble.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<AttachedFile, Long> {
}
