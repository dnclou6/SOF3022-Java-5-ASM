package com.example.AsmGD1.controller;

import com.example.AsmGD1.entity.Cart;
import com.example.AsmGD1.entity.Product;
import com.example.AsmGD1.service.CartService;
import com.example.AsmGD1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String viewCart(Model model) {
        List<Cart> cartItems = cartService.getCart();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotal(cartItems));
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Integer productId) {
        Optional<Product> productOpt = productService.getProductById(productId);
        productOpt.ifPresent(cartService::addToCart);
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartId}")
    public String updateCart(@PathVariable("cartId") Integer cartId, @RequestParam("quantity") Integer quantity) {
        cartService.updateCart(cartId, quantity);
        return "redirect:/cart";
    }


    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Integer id) {
        cartService.deleteCartItem(id);
        return ResponseEntity.ok().build();
    }






    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
