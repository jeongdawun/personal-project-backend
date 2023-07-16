package com.happycamper.backend.product.service;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.product.controller.form.CampsiteVacancyByMapRequestForm;
import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.controller.form.StockRequestForm;
import com.happycamper.backend.product.entity.*;
import com.happycamper.backend.product.repository.*;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.product.service.response.*;
import com.happycamper.backend.utility.number.NumberUtils;
import com.happycamper.backend.utility.transform.TransformToDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                            optionsList.get(i).get(j).getCampsiteVacancy()
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
    public void delete(String email, Long id) {

        Optional<Member> maybeMember = memberRepository.findByEmail(email);

        if(maybeMember.isPresent()) {
            Optional<Product> maybeProduct = productRepository.findProductById(id);
            Member memberByProduct = maybeProduct.get().getMember();
            if(memberByProduct.getId().equals(maybeMember.get().getId())) {
                List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(id);
                for(ProductOption productOption : productOptionList) {
                    optionsRepository.deleteAllByProductOptionId(productOption.getId());
                }
                productOptionRepository.deleteAllByProductId(id);
                productImageRepository.deleteAllByProductId(id);
                productMainImageRepository.deleteByProductId(id);
                productRepository.deleteById(id);
            }
            else {
                log.info("Cannot delete");
            }
        }
    }

    @Override
    public List<ProductListResponseForm> list() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductListResponseForm> responseFormList = new ArrayList<>();

        for(Product product : productList) {
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

            List<Integer> optionPriceList = new ArrayList<>();

            for(ProductOption productOption: productOptionList) {
                optionPriceList.add(productOption.getOptionPrice());
            }
            int minPrice = NumberUtils.findMinValue(optionPriceList);

            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());

            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getId(),
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        minPrice);
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

        List<ProductOptionResponseForm> responseFormList = new ArrayList<>();

        for(ProductOption productOption: productOptionList) {
            ProductOptionResponseForm responseForm =
                    new ProductOptionResponseForm(productOption.getId(), productOption.getOptionName(), productOption.getOptionPrice());
            responseFormList.add(responseForm);
        }

        // 3. 상품 이미지 모두 찾아서 리스트에 담기
        List<ProductImage> productImagesList = productImageRepository.findAllByProductId(product.getId());

        return new ProductReadResponseForm(product, responseFormList,  productImagesList);
    }

    @Override
    public StockResponseForm checkStock(StockRequestForm requestForm) {
        String chkin = requestForm.getCheckInDate();
        String chkout = requestForm.getCheckOutDate();
        LocalDate CheckInDate = TransformToDate.transformToDate(chkin);
        LocalDate CheckOutDate = TransformToDate.transformToDate(chkout);

        Long productId = requestForm.getId( );

        List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(productId);
        log.info("is present? : " + productOptionList.get(0) + productOptionList.get(1));

        // 최종적으로 반환할 옵션들의 명칭과 빈자리 개수
        List<String> optionNameList = new ArrayList<>();
        List<Integer> finalcampsiteVacancyList = new ArrayList<>();

        // 사용자가 선택한 상품의 모든 옵션을 돌면서
        for(ProductOption productOption: productOptionList) {

            // 우선 최종적으로 반환할 옵션명 리스트에 넣어주고
            optionNameList.add(productOption.getOptionName());

            // 해당 옵션의 id로 options를 찾아서 optionsList에 넣는다. (date-stock 리스트 형태)
            List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

            // 해당 options들이 가진 빈자리 개수를 넣을 stockList를 선언한다.
            List<Integer> stockList = new ArrayList<>();

            // 상품 A의 옵션 A가 가진 options(date-stock) 리스트를 돌면서
            for(Options options: optionsList) {
                log.info("there is options");
                // 만약 그 date가 사용자가 원하는 date 내에 해당한다면
                if (options.getDate().isEqual(CheckInDate) || (options.getDate().isAfter(CheckInDate) && options.getDate().isBefore(CheckOutDate))) {
                    log.info("there is valid date");
                    // stockList에 넣는다.
                    stockList.add(options.getCampsiteVacancy());
                }
            }
            // 사용자가 원하는 date 내에 해당하는 빈자리 개수 중 가장 낮은 값을 추출하고
            // 최종적으로 반환할 빈자리 리스트에 넣어준다.
            int min = NumberUtils.findMinValue(stockList);
            finalcampsiteVacancyList.add(min);
        }
        StockResponseForm responseForm = new StockResponseForm(optionNameList, finalcampsiteVacancyList);
        log.info("값이 궁금해: " + responseForm.getOptionNameList() + " " + responseForm.getCampsiteVacancyList());
        return responseForm;
    }

    @Override
    public List<ProductListResponseForm> listByCategory(String category) {
        List<Product> productList = productRepository.findAllByCategory(category);

        List<ProductListResponseForm> responseFormList = new ArrayList<>();

        for(Product product : productList) {
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

            List<Integer> optionPriceList = new ArrayList<>();

            for(ProductOption productOption: productOptionList) {
                optionPriceList.add(productOption.getOptionPrice());
            }
            int minPrice = NumberUtils.findMinValue(optionPriceList);

            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());
            
            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getId(),
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        minPrice);
                responseFormList.add(responseForm);
            }
        }
        return responseFormList;
    }

    @Override
    public List<CampsiteVacancyByMapResponseForm> checkVacancyByDate(CampsiteVacancyByMapRequestForm requestForm) {
        // 클라이언트로부터 체크인, 체크아웃 날짜를 받아서 localDate 형식으로 변환
        String chkin = requestForm.getCheckInDate();
        String chkout = requestForm.getCheckOutDate();
        LocalDate CheckInDate = TransformToDate.transformToDate(chkin);
        LocalDate CheckOutDate = TransformToDate.transformToDate(chkout);

        log.info("Period: " + CheckInDate + " ~ " + CheckOutDate);

        // 모든 상품을 찾아서 list에 넣는다.
        List<Product> productList = productRepository.findAll();
        log.info("The Number of Products " + productList.size());

        // 반환할 양식을 초기화한다.
        List<CampsiteVacancyByMapResponseForm> responseFormList = new ArrayList<>();

        // 첫번째 상품을 돌면서
        for(Product product: productList) {
            // 해당 상품 옵션의 재고를 list에 저장할 것이다.
            // 옵션 A - 5개 / 옵션 B - 3개 라고 한다면 list는 [5, 3]
            List<Integer> allOptionsStockList = new ArrayList<>();

            // 상품 id로 모든 옵션을 찾아서 list에 저장할 것이다.
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());
            log.info("The Number of Options " + productOptionList.size());

            // 첫번째 옵션을 돌면서
            for(ProductOption productOption: productOptionList) {
                // 해당 옵션의 재고를 list에 저장할 것이다.
                List<Integer> stockList = new ArrayList<>();

                // 해당 옵션의 id로 모든 재고를 찾아서 list에 저장할 것이다.
                List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

                // 그 재고 리스트를 돌면서
                for(Options options: optionsList) {

                    // 만약 받아온 체크인 날짜와 체크아웃 날짜 사이에 재고가 존재한다면
                    if (options.getDate().isEqual(CheckInDate) || (options.getDate().isAfter(CheckInDate) && options.getDate().isBefore(CheckOutDate))) {
                        log.info("There is valid date");

                        // 해당 옵션의 재고 list에 저장할 것이다.
                        stockList.add(options.getCampsiteVacancy());
                    }
                }
                // 첫번째 재고 리스트의 순환이 끝나면
                // 재고 리스트의 값 중 가장 낮은 값을 min에 넣을 것이다.
                int min = NumberUtils.findMinValue(stockList);
                // 그 다음 그것을 해당 상품의 옵션 리스트에 넣을 것이다.
                allOptionsStockList.add(min);
            }
            // 옵션에 대한 모든 재고 파악이 끝났다면
            // 두 옵션에 대한 재고를 더해서 allstock에 저장할 것이다.
            int vacancies = 0;
            for (int stock : allOptionsStockList) {
                vacancies += stock;
                log.info("Add vacancy " + vacancies);
            }
            log.info("Total vacancies: " + vacancies);

            CampsiteVacancyByMapResponseForm responseForm =
                    new CampsiteVacancyByMapResponseForm(product.getId(), vacancies, product.getAddress());
            responseFormList.add(responseForm);
        }
        return responseFormList;
    }

    @Override
    public MyProductListResponseForm myList(String email) {

        // 판매자 계정 찾기
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isPresent()) {
            Member member = maybeMember.get();

            // 판매자 계정으로 등록된 상품 찾기
            Optional<Product> maybeProduct = productRepository.findByMember(member);
            if(maybeProduct.isPresent()) {
                Product product = maybeProduct.get();
                System.out.println("product is present");

                List<ProductOptionWithVacancyResponseForm> responseFormList = new ArrayList<>();
                List<ProductOptionResponseForm> responseFormList1 = new ArrayList<>();

                // 해당 상품의 옵션 리스트를 모두 찾기
                List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

                System.out.println("productOptionList is present");
                for(ProductOption productOption: productOptionList) {

                    ProductOptionResponseForm productOptionResponseForm =
                            new ProductOptionResponseForm(productOption.getId(), productOption.getOptionName(), productOption.getOptionPrice());

                    // 해당 옵션의 빈자리 리스트 찾기
                    List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

                    System.out.println("optionsList is present");
                    for(Options options: optionsList) {
                        List<LocalDate> dateList = new ArrayList<>();
                        List<Integer> campsiteVacancyList = new ArrayList<>();

                        dateList.add(options.getDate());
                        campsiteVacancyList.add(options.getCampsiteVacancy());
                        ProductOptionWithVacancyResponseForm responseForm =
                                new ProductOptionWithVacancyResponseForm(
                                        productOption.getId(),
                                        dateList, campsiteVacancyList);
                        responseFormList.add(responseForm);
                    }
                    responseFormList1.add(productOptionResponseForm);
                }

                ProductMainImage productMainImage = productMainImageRepository.findByProductId(product.getId());
                List<ProductImage> productImagesList = productImageRepository.findAllByProductId(product.getId());

                return new MyProductListResponseForm(product, responseFormList1, responseFormList, productMainImage, productImagesList);
            }
        }
        return null;
    }
}
