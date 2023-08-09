package com.example.test.Controller;

import com.example.test.Entity.Product;
import com.example.test.Entity.Type;
import com.example.test.Exception.TypeNotFoundException;
import com.example.test.Repository.TypeRepository;
import com.example.test.Service.ProductService;
import com.example.test.Service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TypeController {
    @Autowired
    ProductService productService;
    private final TypeRepository typeRepository;

    private final TypeService typeService;


    public TypeController(TypeRepository typeRepository, TypeService typeService) {
        this.typeRepository = typeRepository;
        this.typeService = typeService;
    }

    @GetMapping("/type")
    public String getRegister(Model model) {
//        model.addAttribute("typeList",typeRepository.findAll() );
        model.addAttribute("typeList",typeService.listAll() );
        return "Type/View";
    }
    @GetMapping("/typeCreate")
    public String typeCreate(Model model) {
        model.addAttribute("type",new Type() );
        return "Type/Create";
    }
    @GetMapping("/typeDelete")
    public String typeDelete(Model model, @RequestParam Integer id, RedirectAttributes redirectAttributes ) {
//        List<Product> products = productRepository.findAll();
        List<Product> products = productService.listAll();
        List<Type> typeList = new ArrayList<>();
        for (Product product : products) {
            typeList.add(product.getType());
            System.out.println(product.getType().getName());
        }

            for (Product product : products) {
                if (typeRepository.getOne(id).getName() == product.getType().getName()) {
                    model.addAttribute("mess", "Loại sản phẩm không được xóa !");
                    model.addAttribute("typeList",typeService.listAll() );
                    return "Type/View";
                }
        }
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("mess", "Bạn đã xóa thành công");
        return "redirect:/type";
    }
    @GetMapping("/searchType")
    public String searchType(Model model, @RequestParam String keyword) {
//        model.addAttribute("typeList",typeRepository.findAllByNameContaining(keyword) );
        model.addAttribute("typeList",typeService.findAllByTypeName(keyword) );
        return "Type/View";
    }

    @PostMapping("/typeCreate")
    public String typeCreate1(Model model,@ModelAttribute Type type) {
        try {
            typeService.saveType(type);
            return "redirect:/type";
        }catch (Exception ex){
            model.addAttribute("mess","K thành công!");
            return "redirect:/typeCreate";
        }
    }

    @GetMapping("/typeEdit")
    public String typeEdit(Model model, @RequestParam Integer id) throws TypeNotFoundException {
//        model.addAttribute("type",typeRepository.getOne(id) );
        model.addAttribute("type",typeService.getType(id) );
        return "Type/Edit";
    }

    @PostMapping("/typeEdit")
    public String typeEdit(Model model, @ModelAttribute Type type) {
        try {
//            typeRepository.save(type);
            typeService.saveType(type);
            return "redirect:/type";
        }catch (Exception ex){
            return "redirect:/type";
        }

    }
}
