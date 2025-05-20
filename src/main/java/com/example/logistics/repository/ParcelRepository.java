package com.example.logistics.repository;

import com.example.logistics.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}
