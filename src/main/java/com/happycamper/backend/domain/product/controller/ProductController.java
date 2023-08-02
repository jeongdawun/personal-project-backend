package com.happycamper.backend.domain.product.controller;

import com.happycamper.backend.domain.product.controller.form.*;
import com.happycamper.backend.domain.member.service.MemberService;
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

    // 상품 리스트 가져오기(완료)
    @GetMapping("/list")
    public List<ProductListResponseForm> productList () {
        List<ProductListResponseForm> ProductList = productService.list();
        return ProductList;
    }

    // 예약건 기준 상위 8개 상품 리스트 가져오기(완료)
    @GetMapping("/topList")
    public List<ProductListResponseForm> productTopList () {
        List<ProductListResponseForm> ProductList = productService.topList();
        return ProductList;
    }

    // 판매자가 상품 등록시 상품명 중복 확인 - 현재 미사용
    @GetMapping("/check-product-name-duplicate")
    public Boolean checkProductNameDuplicate(@RequestParam("productname") String productname) {
        Boolean isDuplicatedProductName = productService.checkProductNameDuplicate(productname);
        return isDuplicatedProductName;
    }

    // 판매자가 상품 등록(완료)
    @PostMapping("/register")
    public Boolean registerProduct(HttpServletRequest request, @RequestBody ProductRegisterRequestForm requestForm) {
        String email = memberService.extractEmailByCookie(request);
        return productService.register(email, requestForm.toProductRegisterRequest(), requestForm.toProductOptionRegisterRequest());
    }

    // 판매자가 자신이 등록한 상품을 확인(완료)
    @GetMapping("/myList")
    public MyProductListResponseForm myProductList (HttpServletRequest request) {
        String email = memberService.extractEmailByCookie(request);
        return productService.myList(email);
    }

    // 판매자가 자신이 등록한 상품을 수정(완료)
    @PutMapping("/{id}")
    public Boolean modifyProduct (HttpServletRequest request,
                                  @PathVariable("id") Long id,
                                  @RequestBody ProductModifyRequestForm requestForm) {
        String email = memberService.extractEmailByCookie(request);
        return productService.modify(email, id, requestForm.toProductModifyRequest(), requestForm.toProductOptionModifyRequest());
    }

    // 판매자가 자신이 등록한 상품을 삭제(완료)
    @DeleteMapping("/{id}")
    public Boolean deleteProduct(HttpServletRequest request, @PathVariable("id") Long id) {
        String email = memberService.extractEmailByCookie(request);
        return productService.delete(email, id);
    }

    // 상품 읽기(완료)
    @GetMapping("/{id}")
    public ProductReadResponseForm readProduct(@PathVariable("id") Long id) {
        return productService.read(id);
    }

    // 빈자리 조회(완료)
    @PostMapping("/check-vacancy")
    public VacancyResponseForm checkVacancy(@RequestBody VacancyRequestForm requestForm) {
        return productService.checkVacancy(requestForm);
    }

    // 특정 기간의 빈자리 조회(완료)
    @PostMapping("/check-vacancy-by-date")
    public List<CampsiteVacancyByMapResponseForm> checkVacancyByDate(@RequestBody CampsiteVacancyByMapRequestForm requestForm) {
        return productService.checkVacancyByDate(requestForm);
    }

    // 카테고리별 상품 목록(완료)
    @GetMapping("/category/{category}")
    public List<ProductListResponseForm> productListByCategory(@PathVariable("category") String category) {
        List<ProductListResponseForm> productListByCategory = productService.listByCategory(category);
        return productListByCategory;
    }

    // 키워드로 상품 찾기(완료)
    @GetMapping("/search/{keyword}")
    public List<ProductListResponseForm> productListByKeyword(@PathVariable("keyword") String keyword) {
        List<ProductListResponseForm> productListByKeyword = productService.listByKeyword(keyword);
        return productListByKeyword;
    }

}
