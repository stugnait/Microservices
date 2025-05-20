package com.example.parcel.repository;

import com.example.parcel.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}