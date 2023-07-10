package com.happycamper.backend.product.controller;

import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.service.ProductService;
import com.happycamper.backend.product.service.response.ProductListResponseForm;
import com.happycamper.backend.product.service.response.ProductReadResponseForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PostMapping("/register")
//    public Boolean registerProduct(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductRegisterRequestForm requestForm) {
//
//        AuthResponse authResponse = memberService.authorize(request, response);
//        String email = authResponse.getEmail();
//        return productService.register(email, requestForm.toProductRegisterRequest(), requestForm.toProductOptionRegisterRequest());
//    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
    }

    @GetMapping("/list")
    public List<ProductListResponseForm> productList () {
        List<ProductListResponseForm> ProductList = productService.list();
        return ProductList;
    }

    @GetMapping("/{id}")
    public ProductReadResponseForm readProduct(@PathVariable("id") Long id) {
        return productService.read(id);
    }
}
