package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService _productService;

    public AdminController(ProductService productService) {
        _productService = productService;
    }

    @GetMapping("/index")
    public String showAdminPage(Model model) {
        model.addAttribute("currentPage", "dashboard");
        return "admin/index";
    }
    // nguyen minh chau 12345464745564

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("currentPage", "user");
        return "admin/user";
    }

    @GetMapping("/order")
    public String order(Model model) {
        model.addAttribute("currentPage", "order");
        return "admin/order";
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("currentPage", "product");
        List<Product> pdl = _productService.getAllProducts();
        model.addAttribute("products", pdl);
        return "admin/product";
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("currentPage", "category");
        return "admin/category";
    }

    @GetMapping("/brand")
    public String brand(Model model) {
        model.addAttribute("currentPage", "brand");
        return "admin/brand";
    }
}
