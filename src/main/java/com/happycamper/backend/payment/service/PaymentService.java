package com.happycamper.backend.payment.service;

import com.happycamper.backend.payment.service.reponse.KakaoApproveResponse;
import com.happycamper.backend.payment.service.reponse.KakaoCancelResponse;
import com.happycamper.backend.payment.service.reponse.KakaoReadyResponse;

public interface PaymentService {

    KakaoReadyResponse kakaoPayReady(String partner_order_id, String partner_user_id, String item_name, String quantity, String total_amount, String vat_amount);
    KakaoApproveResponse approveResponse(String pgToken, String partner_order_id,  String partner_user_id);
//    KakaoCancelResponse kakaoCancel();
}
