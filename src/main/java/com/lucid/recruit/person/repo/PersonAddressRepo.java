package com.lucid.recruit.person.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucid.recruit.person.entity.PersonAddress;

public interface PersonAddressRepo extends JpaRepository<PersonAddress, String> {

}
