package com.happycamper.backend.product.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductModifyRequest {
    final private String productDetails;
    final private List<String> imageNameList;
}
