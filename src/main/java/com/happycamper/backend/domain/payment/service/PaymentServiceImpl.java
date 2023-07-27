package com.happycamper.backend.domain.payment.service;

import com.happycamper.backend.domain.payment.entity.Payment;
import com.happycamper.backend.domain.payment.repository.PaymentRepository;
import com.happycamper.backend.domain.payment.service.reponse.KakaoApproveResponse;
import com.happycamper.backend.domain.payment.service.reponse.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService{

    final private PaymentRepository paymentRepository;

    // 카카오페이 api 테스트 cid 값
    static final String cid = "TC0ONETIME";

    // 카카오페이 api admin_key(내 애플리케이션 -> 앱 키 4번째)
    @Value("${kakaopay.admin.key}")
    private String admin_Key;
    @Value("${kakaopay.approval.url}")
    private String approval_url;
    @Value("${kakaopay.cancel.url}")
    private String cancel_url;
    @Value("${kakaopay.fail.url}")
    private String fail_url;
    private KakaoReadyResponse kakaoReady;

    // 카카오로 결제 요청
    public KakaoReadyResponse kakaoPayReady(
            String partner_order_id,
            String partner_user_id,
            String item_name,
            String quantity,
            String total_amount,
            String vat_amount) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", partner_order_id);
        parameters.add("partner_user_id", partner_user_id);
        parameters.add("item_name", item_name);
        parameters.add("quantity", quantity);
        parameters.add("total_amount", total_amount);
        parameters.add("vat_amount", vat_amount);
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", approval_url + partner_order_id + "/" + partner_user_id); // 성공 시 redirect url
        parameters.add("cancel_url", cancel_url); // 취소 시 redirect url
        parameters.add("fail_url", fail_url); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        // 카카오로 결제 요청 후 응답값을 KakaoReadyResponse로 받아온다.
        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        return kakaoReady;
    }

    // 카카오로 결제 승인 요청
    public KakaoApproveResponse approveResponse(String pgToken, String partner_order_id, String partner_user_id) {

        // 카카오페이 요청 양식
        // 결제 요청시 보낸 값들과 모두 일치하여야 함
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", partner_order_id);
        parameters.add("partner_user_id", partner_user_id);
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        // 카카오로 결제 승인 요청 후 응답값을 KakaoApproveResponse로 받아온다.
        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        Payment payment = new Payment(
                approveResponse.getTid(),
                approveResponse.getCid(),
                approveResponse.getPartner_order_id(),
                approveResponse.getPartner_user_id(),
                approveResponse.getPayment_method_type(),
                approveResponse.getAmount(),
                approveResponse.getItem_name(),
                approveResponse.getQuantity(),
                approveResponse.getApproved_at()
                );

        paymentRepository.save(payment);

        return approveResponse;
    }

    // 카카오로 요청시 보내야할 headers
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
