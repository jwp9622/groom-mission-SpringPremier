package com.example.WebServiceBasic.domain.boards.repository;

import com.example.WebServiceBasic.domain.boards.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //@Override
    Page<Board> findAll(Pageable pageable);

    Page<Board> findByNameContaining(String keyword, Pageable pageable);

    Optional<Board> findByCode(String code);

    boolean existsByCode(String code);

}
