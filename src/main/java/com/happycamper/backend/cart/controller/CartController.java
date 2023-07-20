package com.happycamper.backend.cart.controller;

import com.happycamper.backend.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.cart.service.CartService;
import com.happycamper.backend.cart.service.response.CartItemListResponseForm;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
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
    public Boolean deleteCartItem(HttpServletRequest request, @PathVariable("id") Long id) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.delete(email, id);
    }

    @GetMapping("/myCart")
    public List<CartItemListResponseForm> getCartItem(HttpServletRequest request) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return cartService.getMyCart(email);
    }
}
