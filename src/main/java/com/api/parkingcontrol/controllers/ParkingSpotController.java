package com.api.parkingcontrol.controllers;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.parkingcontrol.dtos.ParkingSpotDTO;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "", maxAge = 3600) //Perimite o acesso de qualquer origem
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    @Autowired
    ParkingSpotService parkingSpotService; //injeta automaticamente dependencias do service no controller

    @PostMapping // Sem definição de URL pois o post será feito na URL da classe // Método POST
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){ //@Valid serve para validar os paramentros recebidos caso validação for invalida return BAD_Request automaticamente
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is Alredy in use!");
        }

        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Alredy in use!");
        }

        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registred for this apartment/block!");
        }
        var parkingSpotModel = new ParkingSpotModel(); //Instanciação do ParkingSpotModel
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel); //Conversão de DTO para MODEL pois é salvo no BD o somente o MODEL
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC"))); //Setando a data de registro no MODEL
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel)); //Retorno do STATUS e no body salvando no banco de dados com o método SAVE
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){ // se esse optional não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){ // se esse optional não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted Succesfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDTO parkingSpotDTO){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){ // se esse optional não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }
    
}