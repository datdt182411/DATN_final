//package com.example.test.Service.Impl;
//
//import com.example.test.Entity.Maintenance;
//import com.example.test.Exception.MaintenanceNotFoundException;
//import com.example.test.Repository.MaintenanceRepository;
//import com.example.test.Service.MaintenanceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MaintenanceServiceImpl implements MaintenanceService {
//    @Autowired
//    MaintenanceRepository maintenanceRepository;
//
//    @Override
//    public Maintenance getMaintenance(Integer id) throws MaintenanceNotFoundException {
//        Optional<Maintenance> result = maintenanceRepository.findById(id);
//        if (result.isPresent()) {
//            return result.get();
//        }
//        throw new MaintenanceNotFoundException("Could not find any product with ID " + id);
//    }
//
//    @Override
//    public Maintenance saveMaintenance(Maintenance maintenance) {
//        return maintenanceRepository.save(maintenance);
//    }
//
//    @Override
//    public void deleteMaintenance(Integer id) {
//        maintenanceRepository.delete(maintenanceRepository.getOne(id));
//    }
//
//
//    @Override
//    public List<Maintenance> listAll() {
//        return (List<Maintenance>) maintenanceRepository.findAll();
//    }
//
//    @Override
//    public void maintenanceStatus() {
//        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
//
//        LocalDate now = LocalDate.now();
//        System.out.println(now);
//
//        for (Maintenance maintenance : maintenanceList) {
//            if(maintenance.getEndDate().compareTo(String.valueOf(now)) >= 0 ){
//                maintenance.setStatus(0);
//                maintenanceRepository.save(maintenance);
//            }
//            else maintenance.setStatus(1);
//            maintenanceRepository.save(maintenance);
//        }
//    }
//
//    @Override
//    public String findOrderCodeByMaintenanceId(Integer maintenanceId) {
//        return maintenanceRepository.findOrderCodeByMaintenanceId(maintenanceId);
//    }
//
//
////    @Override
////    public List<Maintenance> maintenanceStatus() {
////        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
////        java.util.Date date = new java.util.Date();
////        for (Maintenance maintenance : maintenanceList){
////            if(maintenance.getEndDate().compareTo(String.valueOf(date))){
////
////            }
////        }
////
////
////        return ;
////    }
//
//}
