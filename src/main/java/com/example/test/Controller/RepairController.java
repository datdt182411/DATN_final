package com.example.test.Controller;

import com.example.test.Entity.Product;
import com.example.test.Entity.Repair;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Exception.RepairNotFoundException;
import com.example.test.Repository.ProductRepository;
import com.example.test.Repository.RepairRepository;
import com.example.test.Service.ProductService;
import com.example.test.Service.RepairService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class RepairController {
    private final RepairRepository repairRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;

    private final RepairService repairService;


    public RepairController(RepairRepository repairRepository,
                            ProductRepository productRepository, ProductService productService, RepairService repairService) {
        this.repairRepository = repairRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.repairService = repairService;
    }

    @GetMapping("/Repair")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRepairList(Model model) {
        List<Repair> listRepair = repairService.listAll();
//        model.addAttribute("ItemRepairDtoList", repairRepository.findAll());
        model.addAttribute("listRepair", listRepair);
        return "Item Repair/View";
    }

    @GetMapping("/ItemRepairCreate")
    public String viewRepair(Model model) {
        model.addAttribute("repair", new Repair());
        return "Item Repair/Create";
    }

    @PostMapping("/ItemRepairCreate")
    public String createRepair(Model model, @ModelAttribute Repair repair,
                               @RequestParam Integer productId, RedirectAttributes redirectAttributes) {
        Product product = new Product();
        Optional<Product> optionalProduct= Optional.of(productRepository.findById(productId).orElse(product));
        try {
            if (optionalProduct.equals(product) ) {
                redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm!");
                return "redirect:/ItemRepairCreate";
            } else {
//                repair.setProduct(productRepository.getOne(productId));
                repair.setProduct(productService.getProduct(productId));
//                repairRepository.save(repair);
                repairService.saveRepair(repair);
                redirectAttributes.addFlashAttribute("success", "Thông tin bảo hành đã được thêm thành công!");
                return "redirect:/Repair";

            }
        } catch (Exception | ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm, vui lòng nhập lại!");
            return "redirect:/ItemRepairCreate";
        }
    }

    @GetMapping("/ItemRepairDelete")
    public String deleteItemRepair (Model model, @RequestParam Integer id){
        repairService.deleteItemRepair(id);
        return "redirect:/Repair";
    }

    @GetMapping("/ItemRepairEdit")
    public String editItemRepair (Model model, @RequestParam Integer id) throws RepairNotFoundException {
        model.addAttribute(repairService.getRepair(id));
        return "Item Repair/Edit";
    }

    @PostMapping("/ItemRepairEdit")
    public String editItemRepair (@RequestParam("id") Integer id, @RequestParam("failure") String failure, @RequestParam("replacementComponent") String replacementComponent,@RequestParam("fixedDate") String fixeddate,@RequestParam("failureDate") String failuredate ,@RequestParam("price") Double price , RedirectAttributes redirectAttributes){
        try {

           Repair repair = repairRepository.getRepairById(id);
           repair.setFailure(failure);
           repair.setReplacementComponent(replacementComponent);
           repair.setFailureDate(failuredate);
           repair.setFixedDate(fixeddate);
           repair.setPrice(price);

           repairService.saveRepair(repair);
        }catch (Exception  e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mess", "Edit information of Item Repair is not successful!");
            return "redirect:/ItemRepairEdit";
        }
        return "redirect:/Repair";
    }

}
