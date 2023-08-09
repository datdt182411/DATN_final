package com.example.test.Service;

import com.example.test.Entity.MaintenanceOrder;
import com.example.test.Exception.MaintenanceOrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaintenanceOrderService {

    public MaintenanceOrder getMaintenance(Integer id) throws MaintenanceOrderNotFoundException;

    public  MaintenanceOrder saveMaintenance(MaintenanceOrder maintenanceOrder);

    public void deleteMaintenance(Integer id);

    public List<MaintenanceOrder> listAll();

    public void maintenanceStatus();
}
