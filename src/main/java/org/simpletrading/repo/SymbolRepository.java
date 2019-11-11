package org.simpletrading.repo;

import org.simpletrading.model.Symbol;
import org.simpletrading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol, Long> {
}
