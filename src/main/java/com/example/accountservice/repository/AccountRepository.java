package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>, AccountRepositoryCustom {

}
