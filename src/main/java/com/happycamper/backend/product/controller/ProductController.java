package com.happycamper.backend.product.controller;

import com.happycamper.backend.member.controller.form.AuthRequestForm;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.controller.form.ProductRegisterRequestForm;
import com.happycamper.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    final private ProductService productService;
    final private MemberService memberService;

    @GetMapping("/check-product-name-duplicate")
    public Boolean checkProductNameDuplicate(@RequestBody CheckProductNameDuplicateRequestForm requestForm) {
        Boolean isDuplicatedProductName = productService.checkProductNameDuplicate(requestForm);

        return isDuplicatedProductName;
    }

    @PostMapping("/register")
    public Boolean registerProduct(@RequestBody ProductRegisterRequestForm requestForm) {

        String accessToken = requestForm.getAccessToken();
        AuthRequestForm authRequestForm = new AuthRequestForm(accessToken);
        String email = memberService.authorize(authRequestForm);
        return productService.register(email, requestForm.toProductRegisterRequest(), requestForm.toProductOptionRegisterRequest());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
    }
}
