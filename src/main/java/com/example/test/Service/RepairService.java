package com.example.test.Service;

import com.example.test.Entity.Repair;
import com.example.test.Exception.RepairNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RepairService {
    public Repair saveRepair(Repair repair);

    public List<Repair> listAll();

    public void deleteItemRepair(Integer id);

    public Repair getRepair(Integer id) throws RepairNotFoundException;
}
