package com.api.parkingcontrol.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.ParkingSpotModel;

//Repository é uma interface
//Quando utiliza o extends JPARepository não é necessario o @Repository (mas coloquei para melhor visualização)
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID>{ //extende o repository para utilizar funções SQL
    
    boolean existsByLicensePlateCar(String licensePlateCar); //Necessario instanciar o metodo no repository antes de utlizar em servicec
    boolean existsByParkingSpotNumber(String parkingSpotNumber);
    boolean existsByApartmentAndBlock(String apartment, String block);
}
