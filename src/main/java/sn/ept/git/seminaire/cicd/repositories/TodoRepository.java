package sn.ept.git.seminaire.cicd.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {}
