package com.example.AsmGD1.service;

import com.example.AsmGD1.entity.Cart;
import com.example.AsmGD1.entity.Product;
import com.example.AsmGD1.entity.Order;
import com.example.AsmGD1.entity.OrderDetail;
import com.example.AsmGD1.repository.CartRepository;
import com.example.AsmGD1.repository.OrderDetailRepository;
import com.example.AsmGD1.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    public CartService(CartRepository cartRepository, OrderDetailRepository orderDetailRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    private final List<Cart> cartItems = new ArrayList<>(); // Lưu giỏ hàng tạm thời

    public List<Cart> addToCart(Product product) {
        boolean exists = false;
        for (Cart item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                exists = true;
                break;
            }
        }
        if (!exists) {
            Cart newItem = new Cart();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItems.add(newItem);
        }
        return cartItems;
    }

    public void updateCart(Integer cartId, Integer quantity) {
        for (Cart item : cartItems) {
            if (item.getId().equals(cartId)) {
                item.setQuantity(quantity);
                return;
            }
        }
        throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
    }


    public void deleteCartItem(Integer id) {
        cartRepository.deleteById(id);
    }




    public List<Cart> getCart() {
        return new ArrayList<>(cartItems);
    }

    public double calculateTotal(List<Cart> cartItems) {
        return cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void checkout() {
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        Order order = new Order();
        order.setTotalPrice(calculateTotal(cartItems));
        order.setStatus("Pending");
        orderRepository.save(order);

        for (Cart cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        clearCart();
    }
}
