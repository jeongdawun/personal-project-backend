package com.happycamper.backend.cart.controller;

import com.happycamper.backend.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.cart.service.CartService;
import com.happycamper.backend.cart.service.response.CartItemListResponseForm;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}