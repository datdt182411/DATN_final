package com.example.test.Controller;

import com.example.test.Converter.ConvertDate;
import com.example.test.Converter.ConvertObject;
//import com.example.test.Entity.Maintenance;
import com.example.test.Entity.MaintenanceOrder;
import com.example.test.Entity.OrderLine;
import com.example.test.Entity.Product;
import com.example.test.Exception.MaintenanceNotFoundException;
import com.example.test.Exception.MaintenanceOrderNotFoundException;
import com.example.test.Exception.OrderLineNotFoundException;
import com.example.test.Export.MaintenanceExcelExporter;
import com.example.test.Repository.MaintenanceOrderRepository;
import com.example.test.Repository.OrderLineRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Service.MaintenanceOrderService;
//import com.example.test.Service.MaintenanceService;
import com.example.test.Service.OrderLineService;
import com.example.test.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MaintenanceController {
    @Autowired
    ConvertObject convert;

    @Autowired
    ConvertDate convertDate;
    private final MaintenanceOrderRepository maintenanceOrderRepository;

    private final MaintenanceOrderService maintenanceOrderService;

    private final OrderLineService orderLineService;

    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

//    private final MaintenanceService maintenanceService;

    private final ProductService productService;

    public MaintenanceController(
                                 MaintenanceOrderRepository maintenanceOrderRepository, MaintenanceOrderService maintenanceOrderService, OrderLineService orderLineService, OrderLineRepository orderLineRepository, ProductRepository productRepository, ProductService productService) {
        this.maintenanceOrderRepository = maintenanceOrderRepository;
        this.maintenanceOrderService = maintenanceOrderService;
        this.orderLineService = orderLineService;
        this.orderLineRepository = orderLineRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }



//    @GetMapping("/Maintenance")
//    public String maintenanceViewDefault(Model model) {
//        Pageable pageable = PageRequest.of(0,5);
//        Page<Maintenance> maintenancePage = maintenanceRepository.findAll(pageable);
//        int totalPages = maintenancePage.getTotalPages();
//        long totalItems = maintenancePage.getTotalElements();
//        model.addAttribute("currentPage", 0);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("totalItems", totalItems);
//        model.addAttribute("maintenanceList", maintenancePage);
//        return "Maintenance/View";
//    }
//
//    @GetMapping("/Maintenance/{pageNumber}")
//    public String maintenanceView(Model model, @PathVariable(value = "pageNumber") int currentPage) {
//        Pageable pageable = PageRequest.of(currentPage - 1, 5);
//        Page<Maintenance> maintenancePage = maintenanceRepository.findAll(pageable);
//        int totalPages = maintenancePage.getTotalPages();
//        long totalItems = maintenancePage.getTotalElements();
//        model.addAttribute("currentPage", 0);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("totalItems", totalItems);
//        model.addAttribute("maintenanceList", maintenancePage);
//        return "Maintenance/View";
//    }


    //Use 16/6/2023
    @GetMapping("/Maintenance")
    public String maintenanceViewDefault(Model model) {
        Pageable pageable = PageRequest.of(0,5);
        Page<MaintenanceOrder> maintenancePage = maintenanceOrderRepository.findAll(pageable);
        int totalPages = maintenancePage.getTotalPages();
        long totalItems = maintenancePage.getTotalElements();
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("maintenanceOrderList", maintenancePage);
        return "Maintenance/View";
    }

//    @GetMapping("/Maintenance")
//    public String maintenance(Model model) {
//
//        List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.listAll();
//
//        model.addAttribute("maintenanceOrderList", maintenanceOrderList);
//        return "Maintenance/View";
//    }

    @GetMapping("/Maintenance/{pageNumber}")
    public String maintenanceView(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        Page<MaintenanceOrder> maintenancePage = maintenanceOrderRepository.findAll(pageable);
        int totalPages = maintenancePage.getTotalPages();
        long totalItems = maintenancePage.getTotalElements();
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("maintenanceOrderList", maintenancePage);
        return "Maintenance/View";
    }


//    @GetMapping("/ItemMaintenanceCreate")
//    public String maintenanceCreate(Model model) {
//        model.addAttribute("maintenance", new Maintenance());
//        return "Maintenance/Create";
//    }

//    @PostMapping("/ItemMaintenanceCreate")
//    public String maintenanceCreate(Model model, @ModelAttribute Maintenance maintenance,
//                                    @RequestParam Integer productId, RedirectAttributes redirectAttributes) {
//        Product product = new Product();
//        Optional<Product> optionalProduct = Optional.of(productRepository.findById(productId).orElse(product));
//        System.out.println(product);
//        System.out.println(optionalProduct);
//        try {
//            if (optionalProduct.equals(product)) {
//                redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm!");
//                return "redirect:/ItemMaintenanceCreate";
//            } else {
////                maintenance.setProduct(productRepository.getOne(productId));
//                maintenance.setProduct(productService.getProduct(productId));
////                maintenanceRepository.save(maintenance);
//                maintenanceService.saveMaintenance(maintenance);
//                redirectAttributes.addFlashAttribute("success", "Thông tin bảo hành đã được thêm thành công!");
//                return "redirect:/Maintenance";
//
//            }
//        } catch (Exception | ProductNotFoundException ex) {
//            redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm, vui lòng nhập lại!");
//            return "redirect:/ItemMaintenanceCreate";
//        }
//    }


    @GetMapping("/ItemMaintenanceCreate")
    public String maintenanceCreate(Model model) {
        model.addAttribute("maintenance", new MaintenanceOrder());
        return "Maintenance/Create";
    }

    @PostMapping("/ItemMaintenanceCreate")
    public String maintenanceCreate(Model model, @ModelAttribute MaintenanceOrder maintenanceOrder,
                                    @RequestParam Integer orderLineId, RedirectAttributes redirectAttributes) {

        try {
//            Optional<Product> optionalProduct = productRepository.findById(productId);
//            System.out.println(optionalProduct);
            Optional<OrderLine> optionalOrderLine = orderLineRepository.findById(orderLineId);
            System.out.println(optionalOrderLine);
            if (!optionalOrderLine.isPresent()) {
                redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm!");
                return "redirect:/ItemMaintenanceCreate";
                // Tiếp tục xử lý với product đã tìm thấy
            } else {
//                Product product = optionalProduct.get();
//                System.out.println(product);
//                System.out.println(optionalProduct);

               OrderLine orderLine = optionalOrderLine.get();
                System.out.println(orderLine);
                System.out.println(optionalOrderLine);
                System.out.println(maintenanceOrder.getOrderLine());
                maintenanceOrder.setOrderLine(orderLineService.getOrderLine(orderLineId));
//                maintenanceOrder.getOrderLine();
                maintenanceOrderService.saveMaintenance(maintenanceOrder);
                redirectAttributes.addFlashAttribute("success", "Thông tin bảo hành đã được thêm thành công!");
                return "redirect:/Maintenance";
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm, vui lòng nhập lại!");
            return "redirect:/ItemMaintenanceCreate";
        } catch (OrderLineNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/ItemMaintenanceEdit")
    public String maintenanceEdit(Model model, @RequestParam(name = "id") Integer id) throws  MaintenanceOrderNotFoundException {
        model.addAttribute("maintenanceOrder", maintenanceOrderService.getMaintenance(id));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return "Maintenance/Edit";
    }

    @PostMapping("ItemMaintenanceEdit")
    public String maintenanceEdit ( @ModelAttribute MaintenanceOrder maintenanceOrder){
        try {
          maintenanceOrderService.saveMaintenance(maintenanceOrder);
          return "redirect:/Maintenance";
        }catch (Exception ex){
            ex.printStackTrace();
            return "redirect:/Maintenance";
            }
        }

    @GetMapping("ItemMaintenanceDelete")
    public String maintenanceDelete(Model model, @RequestParam Integer id){
//        List<Product> productList = productRepository.findAllById(id);
        maintenanceOrderService.deleteMaintenance(id);
        return  "redirect:/Maintenance";
    }

    @GetMapping("/searchByMaintenanceTime")
    public String searchByTime(Model model, @RequestParam String fromDate , @RequestParam String toDate) {
        List<MaintenanceOrder> maintenanceList = maintenanceOrderRepository.maintenance_by_time(fromDate,toDate);

        model.addAttribute("maintenanceList", maintenanceList);
        return "Maintenance/View";
    }

    @GetMapping("/ItemMaintenanceOutdated")
    public String maintenanceOutDate(Model model){
     List<MaintenanceOrder> maintenanceList = maintenanceOrderService.listAll();
     List<MaintenanceOrder> maintenances =  new ArrayList<>();
     maintenanceOrderService.maintenanceStatus();

     for (MaintenanceOrder maintenance: maintenanceList){
         if(maintenance.getStatus() == 1){
             maintenances.add(maintenance);

         }
     }
     model.addAttribute("maintenanceList", maintenances);
     return "Maintenance/Outdated";
    }

    @GetMapping("/ItemMaintenanceOndated")
    public String maintenanceOnDate(Model model){
        List<MaintenanceOrder> maintenanceList = maintenanceOrderService.listAll();
        List<MaintenanceOrder> maintenances = new ArrayList<>();
        maintenanceOrderService.maintenanceStatus();

        for(MaintenanceOrder maintenance: maintenanceList){
            if(maintenance.getStatus() == 0){
                maintenances.add(maintenance);
            }
        }
        model.addAttribute("maintenanceList", maintenances);
        return "Maintenance/Ondated";
    }

    @GetMapping("/Maintenance/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.listAll();

        MaintenanceExcelExporter exporter = new MaintenanceExcelExporter();
        exporter.export(maintenanceOrderList, response);
    }
}


