package com.example.demo.controller;

import org.springframework.stereotype.Controller;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService _orderService;

    public OrderController(OrderService orderService) {
        _orderService = orderService;
    }

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }


    @GetMapping("/index")
    public String order(Model model) {

        List<Order> listOrder = _orderService.getAllOrders();
        model.addAttribute("listOrder", listOrder);
        return "admin/order/order-list";
    }

    @PostMapping("/submit")
    public String submitOrder(String customerName,
                              String customerAddress,
                              String phoneNumber,
                              String customerEmail,
                              String note) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName,customerAddress,phoneNumber,customerEmail,note, cartItems);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
}
