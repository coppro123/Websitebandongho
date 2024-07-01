package com.example.demo.controller;
import com.example.demo.model.Category;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ImageService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private static final String UPLOAD_DIR = "uploads/images/";

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    // Show form to add a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "product/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("files") MultipartFile[] files,
                             BindingResult result, Model model) throws IOException {

        if (result.hasErrors()) {
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/add";
        }

        try {
            // Save the main image
            productService.addProduct(product, file);

            // Save the additional images
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile imgFile : files) {
                // Create the directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename for each image
                String fileName = UUID.randomUUID().toString() + "_" + imgFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Save the image file to disk
                Files.copy(imgFile.getInputStream(), filePath);

                // Create an Image object and associate it with the product
                Image image = new Image("/images/" + fileName, product);
                imageList.add(image);
            }

            // Save the list of images to the database
            imageService.saveAllFilesList(imageList);

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
        List<Image> images = imageService.showListImageByProductId(product.getId());
        model.addAttribute("images", images);
        return "product/edit";
    }

    // Update an existing product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid Product product,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("files") MultipartFile[] files,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product/edit";
        }
        try {
            productService.updateProduct(product, file);

            //delete previous images
            imageService.deleteImageByProductId(id);

            List<Image> imageList = new ArrayList<>();
            for (MultipartFile imgFile : files) {
                // Create the directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename for each image
                String fileName = UUID.randomUUID().toString() + "_" + imgFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Save the image file to disk
                Files.copy(imgFile.getInputStream(), filePath);

                // Create an Image object and associate it with the product
                Image image = new Image("/images/" + fileName, product);
                imageList.add(image);


            }
            imageService.saveAllFilesList(imageList);

        }catch (IOException e) {
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

    @GetMapping("/detail/{id}")
    public String showDetailProduct(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Image> images = imageService.showListImageByProductId(product.getId());
            model.addAttribute("images", images);
            model.addAttribute("product", product);
            return "product/detail";
        } else {
            // Handle the case where the product with the given ID is not found
            return "error/productNotFound";
        }
    }
}
