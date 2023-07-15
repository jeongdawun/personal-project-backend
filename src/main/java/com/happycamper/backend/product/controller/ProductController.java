package com.happycamper.backend.product.controller;

import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.product.controller.form.CampsiteVacancyByMapRequestForm;
import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.controller.form.ProductRegisterRequestForm;
import com.happycamper.backend.product.controller.form.StockRequestForm;
import com.happycamper.backend.product.service.ProductService;
import com.happycamper.backend.product.service.response.CampsiteVacancyByMapResponseForm;
import com.happycamper.backend.product.service.response.ProductListResponseForm;
import com.happycamper.backend.product.service.response.ProductReadResponseForm;
import com.happycamper.backend.product.service.response.StockResponseForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.happycamper.backend.member.entity.RoleType.NORMAL;

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
}
