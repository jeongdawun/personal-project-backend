package com.happycamper.backend.product.controller;

import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.product.controller.form.*;
import com.happycamper.backend.product.service.ProductService;
import com.happycamper.backend.product.service.response.*;
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
    public void deleteProduct(HttpServletRequest request, @PathVariable("id") Long id) {

        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        log.info("what is your email: " + email);
        productService.delete(email, id);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Boolean modifyProduct (HttpServletRequest request,
                                  @PathVariable("id") Long id,
                                  @RequestBody ProductModifyRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return productService.modify(email, id, requestForm.toProductRegisterRequest(), requestForm.toProductOptionModifyRequest());
    }

    @GetMapping("/search/{keyword}")
    public List<ProductListResponseForm> productListByKeyword(@PathVariable("keyword") String keyword) {
        log.info("하냐? " + keyword);
        List<ProductListResponseForm> productListByKeyword = productService.listByKeyword(keyword);
        return productListByKeyword;
    }
}
