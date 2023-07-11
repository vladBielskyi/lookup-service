package com.lookup.service.controller;

import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;
import com.lookup.service.service.BinInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BinInfoControllerTest {

    @Mock
    private BinInfoService binInfoService;

    @Mock
    private Model model;

    @InjectMocks
    private BinInfoController binInfoController;


    @Test
    public void validateCard_addsBinInfoToModelWhenValidationSucceeds() {
        String searchField = "123456789";
        ValidationRequestDTO requestDTO = new ValidationRequestDTO(searchField);
        BinInfoResponseDTO responseDTO = new BinInfoResponseDTO();
        Mockito.when(binInfoService.validateCard(Mockito.any(ValidationRequestDTO.class)))
                .thenReturn(Optional.of(responseDTO));

        String viewName = binInfoController.validateCard(searchField, model);

        Assertions.assertEquals("bin-info", viewName);
        Mockito.verify(model).addAttribute("binInfo", responseDTO);
    }

    @Test
    public void validateCard_addsNullBinInfoToModelWhenValidationFails() {
        String searchField = "123456789";
        ValidationRequestDTO requestDTO = new ValidationRequestDTO(searchField);
        Mockito.when(binInfoService.validateCard(Mockito.any(ValidationRequestDTO.class)))
                .thenReturn(Optional.empty());

        String viewName = binInfoController.validateCard(searchField, model);

        Assertions.assertEquals("bin-info", viewName);
        Mockito.verify(model).addAttribute("binInfo", null);
    }
}
