package com.happycamper.backend.domain.product.controller.form;

import com.happycamper.backend.domain.product.entity.Options;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionRegisterRequestForm {
    final private String startDate;
    final private String endDate;
    final private Integer campsiteVacancy;

    public static List<Options> generateOptionsList(LocalDate startDate, LocalDate endDate, int seatCount) {
        List<Options> optionsList = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            Options option = new Options(currentDate, seatCount);
            optionsList.add(option);
            currentDate = currentDate.plusDays(1);
        }

        return optionsList;
    }
}
