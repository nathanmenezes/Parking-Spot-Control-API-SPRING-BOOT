package com.api.parkingcontrol.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

@Service
public class ParkingSpotService {

    @Autowired //injeta automaticamente dependencias do repository no service
    ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) { //Metodo que salva o Model no BD
        return parkingSpotRepository.save(parkingSpotModel);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar){ //verifica se existe essa placa cadastrada no BD
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber){ //verifica se esta em uso esse spot number
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block){ //verifica se aquele apartamento ja tem uma vaga cadastrada
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingSpotModel> findAll() {
        return parkingSpotRepository.findAll();
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        parkingSpotRepository.delete(parkingSpotModel);
    }
}
