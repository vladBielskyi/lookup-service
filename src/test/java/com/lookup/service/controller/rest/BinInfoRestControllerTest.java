package com.lookup.service.controller.rest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BinInfoRestControllerTest {

    @Mock
    private BinInfoService binInfoService;

    @InjectMocks
    private BinInfoRestController binInfoRestController;


    @Test
    public void validateCard_returnsNotFoundWhenValidationFails() {
        ValidationRequestDTO requestDTO = new ValidationRequestDTO();
        Mockito.when(binInfoService.validateCard(requestDTO))
                .thenReturn(Optional.empty());

        ResponseEntity<BinInfoResponseDTO> response = binInfoRestController.validateCard(requestDTO);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void validateCard_returnsValidationResponseWhenValidationSucceeds() {
        ValidationRequestDTO requestDTO = new ValidationRequestDTO();
        BinInfoResponseDTO responseDTO = new BinInfoResponseDTO();
        Mockito.when(binInfoService.validateCard(requestDTO))
                .thenReturn(Optional.of(responseDTO));

        ResponseEntity<BinInfoResponseDTO> response = binInfoRestController.validateCard(requestDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Assertions.assertEquals(responseDTO, response.getBody());
    }
}
