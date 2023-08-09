package com.example.test.Service.Impl;

import com.example.test.Entity.Repair;
import com.example.test.Exception.RepairNotFoundException;
import com.example.test.Repository.RepairRepository;
import com.example.test.Service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairServiceImpl implements RepairService {
    @Autowired
    RepairRepository repairRepository;
    @Override
    public Repair saveRepair(Repair repair) {
        return repairRepository.save(repair);
    }

    @Override
    public List<Repair> listAll() {
        return (List<Repair>) repairRepository.findAll();
    }

    @Override
    public void deleteItemRepair(Integer id) {
        repairRepository.delete(repairRepository.getOne(id));
    }

    @Override
    public Repair getRepair(Integer id) throws RepairNotFoundException {
        Optional<Repair>result = repairRepository.findById(id);
        if(result.isPresent()){
            return  result.get();
        }
        throw new RepairNotFoundException("Could not find any users with ID " + id);
    }


}
