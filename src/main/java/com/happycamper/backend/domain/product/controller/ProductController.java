package com.happycamper.backend.domain.product.controller;

import com.happycamper.backend.domain.product.controller.form.*;
import com.happycamper.backend.domain.member.service.MemberService;
import com.happycamper.backend.domain.member.service.response.AuthResponse;
import com.happycamper.backend.domain.product.service.response.*;
import com.happycamper.backend.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/register")
    public Boolean registerProduct(HttpServletRequest request, @RequestBody ProductRegisterRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return productService.register(email, requestForm.toProductRegisterRequest(), requestForm.toProductOptionRegisterRequest());
    }

    @DeleteMapping("/{id}")
    public Boolean deleteProduct(HttpServletRequest request, @PathVariable("id") Long id) {

        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        log.info("what is your email: " + email);
        return productService.delete(email, id);
    }

    @GetMapping("/list")
    public List<ProductListResponseForm> productList () {
        List<ProductListResponseForm> ProductList = productService.list();
        return ProductList;
    }

    @GetMapping("/myList")
    public MyProductListResponseForm myProductList (HttpServletRequest request) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        return productService.myList(email);
    }

    @GetMapping("/{id}")
    public ProductReadResponseForm readProduct(@PathVariable("id") Long id) {
        return productService.read(id);
    }

    @PostMapping("/check-stock")
    public StockResponseForm checkStock(@RequestBody StockRequestForm requestForm) {
        return productService.checkStock(requestForm);
    }

    @PostMapping("/map-vacancy")
    public List<CampsiteVacancyByMapResponseForm> checkCampsiteVacancyByDate(@RequestBody CampsiteVacancyByMapRequestForm requestForm) {
        return productService.checkVacancyByDate(requestForm);
    }

    @GetMapping("/category/{category}")
    public List<ProductListResponseForm> productListByCategory(@PathVariable("category") String category) {
        List<ProductListResponseForm> productListByCategory = productService.listByCategory(category);
        return productListByCategory;
    }

    @PutMapping("/{id}")
    public Boolean modifyProduct (HttpServletRequest request,
                                  @PathVariable("id") Long id,
                                  @RequestBody ProductModifyRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return productService.modify(email, id, requestForm.toProductModifyRequest(), requestForm.toProductOptionModifyRequest());
    }

    @GetMapping("/search/{keyword}")
    public List<ProductListResponseForm> productListByKeyword(@PathVariable("keyword") String keyword) {
        List<ProductListResponseForm> productListByKeyword = productService.listByKeyword(keyword);
        return productListByKeyword;
    }

    @GetMapping("/topList")
    public List<ProductListResponseForm> productTopList () {
        List<ProductListResponseForm> ProductList = productService.topList();
        return ProductList;
    }
}
