package com.happycamper.backend.domain.cart.service;

import com.happycamper.backend.domain.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.domain.cart.service.response.CartItemListResponseForm;
import com.happycamper.backend.domain.cart.service.response.CompareCartItemListResponseForm;

import java.util.List;

public interface CartService {
    List<CartItemListResponseForm> add(String email, AddCartItemRequestForm requestForm);
    List<CartItemListResponseForm> delete(String email, Long id);
    List<CartItemListResponseForm> getMyCart(String email);
    List<CartItemListResponseForm> deleteList(String email, List<Long> idList);
    List<CompareCartItemListResponseForm> getMyCartItemsForCompare(String email, List<Long> idList);
}
