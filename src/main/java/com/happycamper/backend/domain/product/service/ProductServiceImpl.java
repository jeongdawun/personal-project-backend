package com.happycamper.backend.domain.product.service;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.service.MemberService;
import com.happycamper.backend.domain.product.controller.form.CampsiteVacancyByMapRequestForm;
import com.happycamper.backend.domain.product.controller.form.OptionsModifyRequestForm;
import com.happycamper.backend.domain.product.controller.form.VacancyRequestForm;
import com.happycamper.backend.domain.product.entity.*;
import com.happycamper.backend.domain.product.repository.*;
import com.happycamper.backend.domain.product.service.request.ProductOptionModifyRequest;
import com.happycamper.backend.domain.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.domain.product.service.response.*;
import com.happycamper.backend.domain.product.service.request.ProductModifyRequest;
import com.happycamper.backend.domain.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.domain.reservation.entity.Reservation;
import com.happycamper.backend.domain.reservation.repository.ReservationRepository;
import com.happycamper.backend.utility.number.NumberUtils;
import com.happycamper.backend.utility.transform.TransformToDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final private ProductRepository productRepository;
    final private ProductOptionRepository productOptionRepository;
    final private ProductImageRepository productImageRepository;
    final private ProductMainImageRepository productMainImageRepository;
    final private ProductFacilityRepository productFacilityRepository;
    final private FacilityRepository facilityRepository;
    final private OptionsRepository optionsRepository;
    final private ReservationRepository reservationRepository;
    final private MemberService memberService;

    // 상품 리스트 가져오기(완료)
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
            int minPriceOfOptions = NumberUtils.findMinValue(optionPriceList);

            ProductMainImage productMainImage = productMainImageRepository.findByProductId(product.getId());

            ProductListResponseForm responseForm = new ProductListResponseForm(
                    product.getId(),
                    product.getProductName(),
                    product.getCategory(),
                    productMainImage.getMainImageName(),
                    minPriceOfOptions);
            responseFormList.add(responseForm);
        }

        return responseFormList;
    }

    // 예약건 기준 상위 8개 상품 리스트 가져오기(완료)
    @Override
    public List<ProductListResponseForm> topList() {
        List<Product> productList = productRepository.findAll();

        Map<Long, Integer> reservationNumberByProduct = new HashMap<>();
        for(Product product: productList) {
            List<Reservation> reservationList = reservationRepository.findAllByProduct(product);
            int totalReservationNumber = reservationList.size();
            reservationNumberByProduct.put(product.getId(), totalReservationNumber);
        }

        // 예약 건수를 내림차순으로 정렬하는 새로운 리스트를 생성
        List<Map.Entry<Long, Integer>> sortedList = new ArrayList<>(reservationNumberByProduct.entrySet());
        sortedList.sort(Map.Entry.<Long, Integer>comparingByValue().reversed());

        // 상위 8개만 가져오기
        List<Map.Entry<Long, Integer>> top8List = sortedList.subList(0, Math.min(8, sortedList.size()));

        // 상위 8개 상품의 id 가져오기
        List<Long> top8ProductIds = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : top8List) {
            top8ProductIds.add(entry.getKey());
        }

        List<Product> top8Products = new ArrayList<>();
        for (Long productId : top8ProductIds) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                top8Products.add(product);
            }
        }

        List<ProductListResponseForm> responseFormList = new ArrayList<>();

        for(Product product : top8Products) {
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

            List<Integer> optionPriceList = new ArrayList<>();

            for(ProductOption productOption: productOptionList) {
                optionPriceList.add(productOption.getOptionPrice());
            }
            int minPriceOfOptions = NumberUtils.findMinValue(optionPriceList);

            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());

            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getId(),
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        minPriceOfOptions);
                responseFormList.add(responseForm);
            }
        }
        return responseFormList;
    }

    // 판매자가 상품 등록시 상품명 중복 확인 - 현재 미사용
    @Override
    public Boolean checkProductNameDuplicate(String productname) {
        final Optional<Product> maybeProduct = productRepository.findByProductName(productname);

        if(maybeProduct.isPresent()) {
            log.debug("Product is present");
            return true;
        }
        return false;
    }

    // 판매자가 상품 등록(완료)
    @Transactional
    @Override
    public Boolean register(String email,
                            ProductRegisterRequest productRegisterRequest,
                            ProductOptionRegisterRequest optionRegisterRequest) {

        final Member member = memberService.findMemberByEmail(email);

        if (member == null) {
            log.debug("Member is empty");
            return false;
        }

        // 1. 상품 생성 및 저장
        Product requestProduct = productRegisterRequest.toProduct();
        requestProduct.setMember(member);
        productRepository.save(requestProduct);

        // 2. 상품 상세 이미지 생성 및 저장
        final List<String> requestProductImageNameList = productRegisterRequest.getImageNameList();

        List<ProductImage> productImageList = new ArrayList<>();

        for (String productImageName: requestProductImageNameList) {
            ProductImage productImage = new ProductImage(productImageName);
            productImage.setProduct(requestProduct);
            productImageList.add(productImage);
        }
        productImageRepository.saveAll(productImageList);

        // 3. 상품 옵션 생성 및 저장(옵션명, 옵션 가격)
        final List<String> requestOptionNameList = optionRegisterRequest.getOptionNameList();
        final List<Integer> requestOptionPriceList = optionRegisterRequest.getOptionPriceList();

        List<ProductOption> productOptionList = new ArrayList<>();

        for (int i = 0; i < requestOptionNameList.size(); i++ ){
            ProductOption productOption = new ProductOption(
                    requestOptionNameList.get(i),
                    requestOptionPriceList.get(i));
            productOption.setProduct(requestProduct);
            productOptionList.add(productOption);
        }
        productOptionRepository.saveAll(productOptionList);

        // 4. 상품 메인 이미지 생성 및 저장
        final String requeestMainImageName = productRegisterRequest.getMainImageName();
        ProductMainImage productMainImage = new ProductMainImage(requeestMainImageName);
        productMainImage.setProduct(requestProduct);

        productMainImageRepository.save(productMainImage);

        // 5. 상품 시설 정보 생성 및 저장
        final List<FacilityType> selectedFacilityTypes = productRegisterRequest.getFacilityType();
        List<Facility> facilities = new ArrayList<>();

        for (FacilityType facilityType : selectedFacilityTypes) {
            Optional<Facility> facility = facilityRepository.findByFacilityType(facilityType);
            if (facility.isPresent()) {
                facilities.add(facility.get());
            }
        }

        List<ProductFacility> requestProductFacility = new ArrayList<>();
        for (Facility facility : facilities) {
            ProductFacility productFacility
                    = new ProductFacility(facility, requestProduct);
            requestProductFacility.add(productFacility);
        }

        productFacilityRepository.saveAll(requestProductFacility);

        // 6. 상품 옵션의 날짜별 빈자리 목록 생성 및 저장
        final List<List<Options>> requestOptionsList = optionRegisterRequest.getOptionsList();

        List<List<Options>> optionsList = new ArrayList<>();

        for(int i = 0; i < requestOptionsList.size(); i++){
            List<Options> optionsListPerOption = new ArrayList<>();
            for(int j = 0; j < requestOptionsList.get(i).size(); j++) {
                Options options = new Options(
                        requestOptionsList.get(i).get(j).getDate(),
                        requestOptionsList.get(i).get(j).getCampsiteVacancy()
                );
                options.setProductOption(productOptionList.get(i));
                optionsListPerOption.add(options);
            }
            optionsList.add(optionsListPerOption);
            optionsRepository.saveAll(optionsListPerOption);
        }

        return true;
    }

    // 판매자가 자신이 등록한 상품을 확인(완료)
    @Override
    public MyProductListResponseForm myList(String email) {

        // 판매자 계정 찾기
        final Member member = memberService.findMemberByEmail(email);

        if(member == null) {
            log.debug("member is empty");
            return null;
        }

        // 판매자 계정으로 등록된 상품 찾기
        Optional<Product> maybeProduct = productRepository.findByMember(member);
        if(maybeProduct.isPresent()) {
            Product product = maybeProduct.get();

            List<ProductOptionWithVacancyResponseForm> productOptionWithVacancyResponseFormList = new ArrayList<>();
            List<ProductOptionResponseForm> productOptionResponseFormList = new ArrayList<>();

            // 해당 상품의 옵션 리스트를 모두 찾기
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

            for(ProductOption productOption: productOptionList) {
                ProductOptionResponseForm productOptionResponseForm =
                        new ProductOptionResponseForm(productOption.getId(), productOption.getOptionName(), productOption.getOptionPrice());

                // 해당 옵션의 빈자리 리스트 찾기
                List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

                for(Options options: optionsList) {
                    ProductOptionWithVacancyResponseForm responseForm =
                            new ProductOptionWithVacancyResponseForm(
                                    productOption.getId(),
                                    options.getDate(),
                                    options.getCampsiteVacancy());
                    productOptionWithVacancyResponseFormList.add(responseForm);
                }
                productOptionResponseFormList.add(productOptionResponseForm);
            }

            // 해당 상품의 메인 이미지, 상세 이미지 찾기
            ProductMainImage productMainImage = productMainImageRepository.findByProductId(product.getId());
            List<ProductImage> productImagesList = productImageRepository.findAllByProductId(product.getId());

            // 해당 상품의 시설정보 모두 찾아서 리스트에 담기
            List<ProductFacility> productFacilityList = productFacilityRepository.findAllByProductId(product.getId());

            List<Facility> facilities = new ArrayList<>();
            for(ProductFacility productFacility: productFacilityList) {
                Optional<Facility> facility = facilityRepository.findById(productFacility.getFacility().getId());

                if(facility.isPresent()) {
                    facilities.add(facility.get());
                }
            }

            return new MyProductListResponseForm(
                    product,
                    productOptionResponseFormList,
                    productOptionWithVacancyResponseFormList,
                    productMainImage,
                    productImagesList,
                    facilities);
        }

        return null;
    }

    // 판매자가 자신이 등록한 상품을 수정(완료)
    @Transactional
    @Override
    public Boolean modify(String email, Long id,
                          ProductModifyRequest productModifyRequest,
                          ProductOptionModifyRequest optionModifyRequest) {

        // 판매자 계정 찾기
        final Member member = memberService.findMemberByEmail(email);

        if(member == null) {
            log.debug("member is empty");
            return false;
        }

        Optional<Product> maybeProduct = productRepository.findWithMemberById(id);
        if(maybeProduct.isEmpty()){
            log.debug("Product is empty");
            return false;
        }

        Member memberByProduct = maybeProduct.get().getMember();
        if(!memberByProduct.getId().equals(member.getId())) {
            log.debug("Not the owner's registered product");
            return false;
        }

        // 1. 수정으로 들어온 상품 상세 정보로 기존 ProductDetails 갱신 후 저장
        Product foundProduct = maybeProduct.get();
        foundProduct.setProductDetails(productModifyRequest.getProductDetails());
        productRepository.save(foundProduct);

        // 2. 수정으로 들어온 이미지 이름 추출하여 기존 ProductImage을 갱신 후 저장
        List<String> modifyProductImageNameList = productModifyRequest.getImageNameList();

        List<ProductImage> foundProductImageList = productImageRepository.findAllByProductId(foundProduct.getId());

        for(int i = 0; i < modifyProductImageNameList.size(); i++) {
            foundProductImageList.get(i).setImageName(modifyProductImageNameList.get(i));
        }
        productImageRepository.saveAll(foundProductImageList);

        // 3. 수정으로 들어온 상품 옵션명, 옵션가격 추출하여 기존 ProductOption을 갱신 후 저장
        List<String> modifyOptionNameList = optionModifyRequest.getOptionNameList();
        List<Integer> modifyOptionPriceList = optionModifyRequest.getOptionPriceList();

        List<ProductOption> foundProductOptionList = productOptionRepository.findAllByProductId(foundProduct.getId());

        for (int i = 0; i < foundProductOptionList.size(); i++ ){
            foundProductOptionList.get(i).setOptionName(modifyOptionNameList.get(i));
            foundProductOptionList.get(i).setOptionPrice(modifyOptionPriceList.get(i));
        }
        productOptionRepository.saveAll(foundProductOptionList);

        // 4. 수정으로 들어온 상품 옵션별 날짜별 빈자리 개수 추출하여 기존 Options를 갱신 후 저장
        List<OptionsModifyRequestForm> modifyOptionsList = optionModifyRequest.getOptionsList();

        Map<Long, List<OptionsModifyRequestForm>> groupedOptionsByProductOptionId = new HashMap<>();

        for (OptionsModifyRequestForm modifyOptionsByProductOption : modifyOptionsList) {
            Long productOptionId = modifyOptionsByProductOption.getId();
            List<OptionsModifyRequestForm> groupedOptionsList = groupedOptionsByProductOptionId.getOrDefault(productOptionId, new ArrayList<>());
            groupedOptionsList.add(modifyOptionsByProductOption);
            groupedOptionsByProductOptionId.put(productOptionId, groupedOptionsList);
        }

        for (int i = 0; i < foundProductOptionList.size(); i++) {
            List<Options> foundOptionsList = optionsRepository.findAllByProductOptionId(foundProductOptionList.get(i).getId());
            List<OptionsModifyRequestForm> groupedList = groupedOptionsByProductOptionId.get(foundProductOptionList.get(i).getId());

            for (int j = 0; j < foundOptionsList.size(); j++) {
                foundOptionsList.get(j).setDate(TransformToDate.transformToDate(groupedList.get(j).getDateList()));
                foundOptionsList.get(j).setCampsiteVacancy(groupedList.get(j).getCampsiteVacancyList());
            }
            optionsRepository.saveAll(foundOptionsList);
        }
        return true;
    }

    // 판매자가 자신이 등록한 상품을 삭제(완료)
    @Transactional
    @Override
    public Boolean delete(String email, Long id) {

        final Member member = memberService.findMemberByEmail(email);

        if(member == null) {
            log.debug("Member is empty");
            return false;
        }

        Optional<Product> maybeProduct = productRepository.findProductById(id);
        if(maybeProduct.isEmpty()) {
            log.debug("Product is empty");
            return false;
        }

        Product product = maybeProduct.get();
        List<Reservation> reservationList = reservationRepository.findAllByProduct(product);
        if(reservationList.size() > 0) {
            log.debug("Reservation is present");
            return false;
        }

        Member memberByProduct = product.getMember();
        if(memberByProduct.getId().equals(member.getId())) {
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(id);
            for(ProductOption productOption : productOptionList) {
                optionsRepository.deleteAllByProductOptionId(productOption.getId());
            }
            productFacilityRepository.deleteAllByProductId(id);
            productOptionRepository.deleteAllByProductId(id);
            productImageRepository.deleteAllByProductId(id);
            productMainImageRepository.deleteByProductId(id);
            productRepository.deleteById(id);

            return true;
        }
        else {
            log.debug("Cannot delete");
            return false;
        }
    }

    // 상품 읽기(완료)
    @Override
    public ProductReadResponseForm read(Long id) {
        Optional<Product> maybeProduct = productRepository.findById(id);

        if (maybeProduct.isEmpty()) {
            log.debug("Product is empty");
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

        // 4. 상품 시설정보 모두 찾아서 리스트에 담기
        List<ProductFacility> productFacilityList = productFacilityRepository.findAllByProductId(product.getId());

        List<Facility> facilities = new ArrayList<>();
        for(ProductFacility productFacility: productFacilityList) {
            Optional<Facility> facility = facilityRepository.findById(productFacility.getFacility().getId());

            if(facility.isPresent()) {
                facilities.add(facility.get());
            }
        }

        return new ProductReadResponseForm(product, responseFormList, productImagesList, facilities);
    }

    // 빈자리 조회(완료)
    @Override
    public VacancyResponseForm checkVacancy(VacancyRequestForm requestForm) {
        final LocalDate CheckInDate = TransformToDate.transformToDate(requestForm.getCheckInDate());
        final LocalDate CheckOutDate = TransformToDate.transformToDate(requestForm.getCheckOutDate());

        final Long productId = requestForm.getId( );

        List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(productId);

        // 최종적으로 반환할 옵션들의 명칭과 빈자리 개수
        List<String> optionNameList = new ArrayList<>();
        List<Integer> finalcampsiteVacancyList = new ArrayList<>();

        // 사용자가 선택한 상품의 모든 옵션을 돌면서
        for(ProductOption productOption: productOptionList) {

            // 우선 최종적으로 반환할 옵션명 리스트에 넣어주고
            optionNameList.add(productOption.getOptionName());

            // 해당 옵션의 id로 options를 찾아서 optionsList에 넣는다. (date-vacancy 리스트 형태)
            List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

            // 해당 options들이 가진 빈자리 개수를 넣을 vacancyList를 선언한다.
            List<Integer> vacancyList = new ArrayList<>();

            // 상품 A의 옵션 A가 가진 options(date-vacancy) 리스트를 돌면서
            for(Options options: optionsList) {
                log.debug("there is options");
                // 만약 그 date가 사용자가 원하는 date 내에 해당한다면
                if (options.getDate().isEqual(CheckInDate) || (options.getDate().isAfter(CheckInDate) && options.getDate().isBefore(CheckOutDate))) {
                    log.debug("there is valid date");
                    // vacancyList 넣는다.
                    vacancyList.add(options.getCampsiteVacancy());
                }
            }
            // 사용자가 원하는 date 내에 해당하는 빈자리 개수 중 가장 낮은 값을 추출하고
            // 최종적으로 반환할 빈자리 리스트에 넣어준다.
            int minVacancy = NumberUtils.findMinValue(vacancyList);
            finalcampsiteVacancyList.add(minVacancy);
        }
        VacancyResponseForm responseForm = new VacancyResponseForm(optionNameList, finalcampsiteVacancyList);
        return responseForm;
    }

    // 특정 기간의 빈자리 조회(완료)
    @Override
    public List<CampsiteVacancyByMapResponseForm> checkVacancyByDate(CampsiteVacancyByMapRequestForm requestForm) {
        // 클라이언트로부터 체크인, 체크아웃 날짜를 받아서 localDate 형식으로 변환
        final LocalDate CheckInDate = TransformToDate.transformToDate(requestForm.getCheckInDate());
        final LocalDate CheckOutDate = TransformToDate.transformToDate(requestForm.getCheckOutDate());

        // 모든 상품을 찾아서 list에 넣는다.
        List<Product> productList = productRepository.findAll();
        log.debug("The Number of Products " + productList.size());

        // 반환할 양식을 초기화한다.
        List<CampsiteVacancyByMapResponseForm> responseFormList = new ArrayList<>();

        // 첫번째 상품을 돌면서
        for(Product product: productList) {
            // 해당 상품 옵션의 재고를 list에 저장할 것이다.
            // 옵션 A - 5개 / 옵션 B - 3개 라고 한다면 list는 [5, 3]
            List<Integer> allOptionsVacancyList = new ArrayList<>();

            // 상품 id로 모든 옵션을 찾아서 list에 저장할 것이다.
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());
            log.debug("The Number of Options " + productOptionList.size());

            // 첫번째 옵션을 돌면서
            for(ProductOption productOption: productOptionList) {
                // 해당 옵션의 재고를 list에 저장할 것이다.
                List<Integer> vacancyList = new ArrayList<>();

                // 해당 옵션의 id로 모든 재고를 찾아서 list에 저장할 것이다.
                List<Options> optionsList = optionsRepository.findAllByProductOptionId(productOption.getId());

                // 그 재고 리스트를 돌면서
                for(Options options: optionsList) {

                    // 만약 받아온 체크인 날짜와 체크아웃 날짜 사이에 재고가 존재한다면
                    if (options.getDate().isEqual(CheckInDate) || (options.getDate().isAfter(CheckInDate) && options.getDate().isBefore(CheckOutDate))) {
                        log.debug("There is valid date");

                        // 해당 옵션의 재고 list에 저장할 것이다.
                        vacancyList.add(options.getCampsiteVacancy());
                    }
                }
                // 첫번째 재고 리스트의 순환이 끝나면
                // 재고 리스트의 값 중 가장 낮은 값을 min에 넣을 것이다.
                int minVacancy = NumberUtils.findMinValue(vacancyList);
                // 그 다음 그것을 해당 상품의 옵션 리스트에 넣을 것이다.
                allOptionsVacancyList.add(minVacancy);
            }
            // 옵션에 대한 모든 재고 파악이 끝났다면
            // 두 옵션에 대한 재고를 더해서 allVacancy에 저장할 것이다.
            int vacancies = 0;
            for (int vacancy : allOptionsVacancyList) {
                vacancies += vacancy;
                log.debug("Add vacancy " + vacancies);
            }
            log.debug("Total vacancies: " + vacancies);

            CampsiteVacancyByMapResponseForm responseForm =
                    new CampsiteVacancyByMapResponseForm(product.getId(), vacancies, product.getAddress());
            responseFormList.add(responseForm);
        }
        return responseFormList;
    }

    // 카테고리별 상품 목록(완료)
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
            int minPriceOfOptions = NumberUtils.findMinValue(optionPriceList);

            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());
            
            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getId(),
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        minPriceOfOptions);
                responseFormList.add(responseForm);
            }
        }
        return responseFormList;
    }

    // 키워드로 상품 찾기(완료)
    @Override
    public List<ProductListResponseForm> listByKeyword(String keyword) {
        List<Product> productList = productRepository.findAllByProductNameContaining(keyword);

        List<ProductListResponseForm> responseFormList = new ArrayList<>();

        for(Product product : productList) {
            List<ProductOption> productOptionList = productOptionRepository.findAllByProductId(product.getId());

            List<Integer> optionPriceList = new ArrayList<>();

            for(ProductOption productOption: productOptionList) {
                optionPriceList.add(productOption.getOptionPrice());
            }
            int minPriceOfOptions = NumberUtils.findMinValue(optionPriceList);

            Optional<ProductMainImage> productMainImage = productMainImageRepository.findById(product.getId());

            if(productMainImage.isPresent()) {
                ProductListResponseForm responseForm = new ProductListResponseForm(
                        product.getId(),
                        product.getProductName(),
                        product.getCategory(),
                        productMainImage.get().getMainImageName(),
                        minPriceOfOptions);
                responseFormList.add(responseForm);
            }
        }
        return responseFormList;
    }
}
