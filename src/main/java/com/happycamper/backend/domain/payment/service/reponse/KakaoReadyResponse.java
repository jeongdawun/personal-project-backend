package com.happycamper.backend.domain.payment.service.reponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoReadyResponse {
    private String tid;                         // 결제 고유 번호, 20자
    private String next_redirect_mobile_url;    // 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
    private String next_redirect_pc_url;        // PC 웹일 경우 카카오톡으로 결제 요청 메시지
    private String created_at;                  // 결제 준비 요청 시간
}
