package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ImageService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService _productService;
    private final CategoryService _categoryService;
    private final ImageService _imageService;
    private static final String UPLOAD_DIR = "uploads/images/";

    public AdminController(ProductService productService, CategoryService categoryService, ImageService imageService) {
        _productService = productService;
        _categoryService = categoryService;
        _imageService = imageService;
    }

    @GetMapping("/index")
    public String showAdminPage(Model model) {
        model.addAttribute("currentPage", "dashboard");
        return "admin/index";
    }
    // nguyen minh chau 12345464745564654

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
        return "admin/product/product_-list";
    }


    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("currentPage", "category");
        return "admin/category";
    }

    @GetMapping("/category/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/add-category";
    }

    @PostMapping("/category/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/category/add-category";
        }
        _categoryService.addCategory(category);
        return "redirect:/admin/category/index";
    }
    @GetMapping("/category/index")
    public String categorylist(Model model) {
        List<Category> categories = _categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "/admin/category/category-list";
    }

    // GET request to show category edit form
    @GetMapping("/category/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = _categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:"
                        + id));
        model.addAttribute("category", category);
        return "/admin/category/update-category";
    }
    // POST request to update category
    @PostMapping("/category/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setId(id);
            return "/admin/category/update-category";
        }
        _categoryService.updateCategory(category);
        model.addAttribute("categories", _categoryService.getAllCategories());
        return "redirect:/admin/category/index";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        Category category = _categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:"
                        + id));
        _categoryService.deleteCategoryById(id);
        model.addAttribute("categories", _categoryService.getAllCategories());
        return "redirect:/admin/category/index";
    }

    @GetMapping("/brand")
    public String brand(Model model) {
        model.addAttribute("currentPage", "brand");
        return "admin/brand";
    }
}