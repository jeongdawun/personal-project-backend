package com.happycamper.backend.cart.service;

import com.happycamper.backend.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.cart.service.response.CartItemListResponseForm;

import java.util.List;

public interface CartService {
    List<CartItemListResponseForm> add(String email, AddCartItemRequestForm requestForm);
    List<CartItemListResponseForm> delete(String email, Long id);
    List<CartItemListResponseForm> getMyCart(String email);
    List<CartItemListResponseForm> deleteList(String email, List<Long> idList);
}
