package com.example.demo.controller;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // Show form to add a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "product/add";
    }

    // Add a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, @RequestParam("file") MultipartFile file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/add";
        }
        try {
            productService.addProduct(product, file);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file upload error
        }
        return "redirect:/product/index";
    }

    // Show form to edit a product
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + id));
        model.addAttribute("product", product);
        return "product/edit";
    }

    // Update an existing product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid Product product, @RequestParam("file") MultipartFile file, BindingResult result, Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/edit";
        }
        try {
            productService.updateProduct(product, file);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file upload error
        }
        return "redirect:/product/index";
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        productService.deleteProductById(id);
        return "redirect:/product/index";
    }

    // Show all products
    @GetMapping("/index")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/index";
    }
}
