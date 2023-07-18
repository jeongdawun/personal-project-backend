package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.product.entity.Options;
import com.happycamper.backend.utility.transform.TransformToDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductOptionModifyRequestForm {
    final private List<String> dateList;
    final private List<Integer> campsiteVacancyList;

    public List<Options> generateOptionsList(List<String> dateList, List<Integer> campsiteVacancyList) {
        List<Options> optionsList = new ArrayList<>();

        for(int i = 0; i < dateList.size(); i++) {
            LocalDate date = TransformToDate.transformToDate(dateList.get(i));
            Options option = new Options(date, campsiteVacancyList.get(i));
            optionsList.add(option);
        }

        return optionsList;
    }
}
