package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    @Query("select h from House h where h.user is null or h.user =:user")
    Page<House> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("select h from House h where h.user is null or h.user =:user")
    List<House> findAllByUser(@Param("user") User user);

    @Query("select h from House h where h.user is null")
    Page<House> findDefault(Pageable pageable);

    @Query("select h from House h where h.user is null")
    List<House> findDefault();
}