package com.example.test.Service.Impl;

import com.example.test.Entity.MaintenanceOrder;
import com.example.test.Exception.MaintenanceOrderNotFoundException;
import com.example.test.Repository.MaintenanceOrderRepository;
import com.example.test.Service.MaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceOrderServiceImpl implements MaintenanceOrderService {

    @Autowired
    MaintenanceOrderRepository maintenanceOrderRepository;

    @Override
    public MaintenanceOrder getMaintenance(Integer id) throws MaintenanceOrderNotFoundException {
        Optional<MaintenanceOrder> result = maintenanceOrderRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        }
        throw new MaintenanceOrderNotFoundException("Could not find any product with ID " + id);
    }

    @Override
    public MaintenanceOrder saveMaintenance(MaintenanceOrder maintenanceOrder) {
        return maintenanceOrderRepository.save(maintenanceOrder);
    }

    @Override
    public void deleteMaintenance(Integer id) {
        maintenanceOrderRepository.delete(maintenanceOrderRepository.getOne(id));

    }

    @Override
    public List<MaintenanceOrder> listAll() {
        return (List<MaintenanceOrder>) maintenanceOrderRepository.findAll();
    }

    @Override
    public void maintenanceStatus() {
        List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderRepository.findAll();

        LocalDate now = LocalDate.now();

        for (MaintenanceOrder maintenanceOrder : maintenanceOrderList) {
            if(maintenanceOrder.getEndDate().compareTo(String.valueOf(now)) >= 0){
                maintenanceOrder.setStatus(0);
                maintenanceOrderRepository.save(maintenanceOrder);
            }
            else maintenanceOrder.setStatus(1);
            maintenanceOrderRepository.save(maintenanceOrder);
        }

    }
}
