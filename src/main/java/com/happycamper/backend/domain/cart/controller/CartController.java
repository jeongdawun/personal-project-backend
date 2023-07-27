package com.happycamper.backend.domain.cart.controller;

import com.happycamper.backend.domain.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.domain.cart.service.CartService;
import com.happycamper.backend.domain.cart.service.response.CartItemListResponseForm;
import com.happycamper.backend.domain.cart.service.response.CompareCartItemListResponseForm;
import com.happycamper.backend.domain.member.service.MemberService;
import com.happycamper.backend.domain.member.service.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    final private CartService cartService;
    final private MemberService memberService;

    @PostMapping("/add")
    public List<CartItemListResponseForm> addCartItem(HttpServletRequest request, @RequestBody AddCartItemRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        // 비즈니스 계정은 장바구니 이용 불가
        if(authResponse.getRole() == "BUSINESS") {
            return null;
        }

        return cartService.add(email, requestForm);
    }

    @DeleteMapping("/{id}")
    public List<CartItemListResponseForm> deleteCartItem(HttpServletRequest request, @PathVariable("id") Long id) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.delete(email, id);
    }

    @DeleteMapping
    public List<CartItemListResponseForm> deleteCartItemList(HttpServletRequest request, @RequestParam("idList") List<Long> idList) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.deleteList(email, idList);
    }

    @GetMapping("/myCart")
    public List<CartItemListResponseForm> getCartItem(HttpServletRequest request) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.getMyCart(email);
    }

    @PostMapping("/compare-cartItems")
    public List<CompareCartItemListResponseForm> getCartItemsForCompare(HttpServletRequest request, @RequestBody List<Long> idList) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.getMyCartItemsForCompare(email, idList);
    }
}
