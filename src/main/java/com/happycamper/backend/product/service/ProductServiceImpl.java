package com.happycamper.backend.product.service;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.entity.*;
import com.happycamper.backend.product.repository.*;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.product.service.response.ProductListResponseForm;
import com.happycamper.backend.product.service.response.ProductReadResponseForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final private ProductRepository productRepository;
    final private ProductOptionRepository productOptionRepository;
    final private ProductImageRepository productImageRepository;
    final private ProductMainImageRepository productMainImageRepository;
    final private OptionsRepository optionsRepository;
    final private MemberRepository memberRepository;

    @Override
    public Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm) {
        Optional<Product> maybeProduct = productRepository.findByProductName(requestForm.getProductName());

        if(maybeProduct.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean register(String email, ProductRegisterRequest productRegisterRequest, ProductOptionRegisterRequest optionRegisterRequest) {

        Optional<Member> maybeMember = memberRepository.findByEmail(email);

        if(maybeMember.isPresent()) {
            Member member = maybeMember.get();

            Product product = productRegisterRequest.toProduct();
            product.setMember(member);

            // 1. 이미지 저장
            List<String> productImageNameList = productRegisterRequest.getImageNameList();

            List<ProductImage> createProductImageList = new ArrayList<>();

            for(int i = 0; i < productImageNameList.size(); i++) {
                ProductImage productImage = new ProductImage(
                        productImageNameList.get(i));
                productImage.setProduct(product);
                createProductImageList.add(productImage);
            }

            // 2. 옵션 저장(옵션명, 옵션 가격)
            List<String> optionNameList = optionRegisterRequest.getOptionNameList();
            List<Integer> optionPriceList = optionRegisterRequest.getOptionPriceList();

            List<ProductOption> createProductOptionList = new ArrayList<>();

            for(int i = 0; i < optionNameList.size(); i++ ){
                ProductOption productOption = new ProductOption(
                        optionNameList.get(i),
                        optionPriceList.get(i));
                productOption.setProduct(product);
                createProductOptionList.add(productOption);
            }

            // 3. 메인 이미지 저장
            String mainImageName = productRegisterRequest.getMainImageName();
            ProductMainImage productMainImage = new ProductMainImage(mainImageName);
            productMainImage.setProduct(product);

            productRepository.save(product);
            productImageRepository.saveAll(createProductImageList);
            productOptionRepository.saveAll(createProductOptionList);
            productMainImageRepository.save(productMainImage);

            // 4. 옵션의 날짜별 재고 저장
            List<List<Options>> optionsList = optionRegisterRequest.getOptionsList();

            List<List<Options>> createOptionsList = new ArrayList<>();

            for(int i = 0; i < optionsList.size(); i++){
                List<Options> optionsList1 = new ArrayList<>();
                for(int j = 0; j < optionsList.get(i).size(); j++) {
                    Options options = new Options(
                            optionsList.get(i).get(j).getDate(),
                            optionsList.get(i).get(j).getStock()
                    );
                    options.setProductOption(createProductOptionList.get(i));
                    optionsList1.add(options);
                }
                createOptionsList.add(optionsList1);
                optionsRepository.saveAll(optionsList1);
            }

            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {

        List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(id);
        for(ProductOption productOption : productOptionList) {
            optionsRepository.deleteAllByProductOptionId(productOption.getId());
        }
        productOptionRepository.deleteAllByProductId(id);
        productImageRepository.deleteAllByProductId(id);
        productMainImageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductListResponseForm> list() {

        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductListResponseForm> responseFormList = new ArrayList<>();

        for(Product product : productList) {
            ProductOption productOption = productOptionRepository.findMinPriceOptionByProductId(product.getId());
            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());
            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        productOption.getOptionPrice());
                responseFormList.add(responseForm);
            }
        }
        return responseFormList;
    }

    @Override
    public ProductReadResponseForm read(Long id) {
        final Optional<Product> maybeProduct = productRepository.findById(id);

        if (maybeProduct.isEmpty()) {
            return null;
        }

        // 1. 상품 가져오기
        Product product = maybeProduct.get();

        // 2. 상품 Id로 상품 옵션 리스트 찾기
        List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

        // 3. 상품 이미지 모두 찾아서 리스트에 담기
        List<ProductImage> productImagesList = productImageRepository.findAllByProductId(product.getId());

        return new ProductReadResponseForm(product, productOptionList,  productImagesList);
    }
}
