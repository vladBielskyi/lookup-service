package com.lookup.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;
import com.lookup.service.entity.BinInfo;
import com.lookup.service.repository.BinInfoRepository;
import com.lookup.service.service.impl.BinInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BinInfoServiceTest {

    @Mock
    private BinInfoRepository binInfoRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ExecutorService executorService;

    @Mock
    private FileService fileService;

    @Mock
    private ZipInputStream zipInputStream;

    @InjectMocks
    private BinInfoServiceImpl binInfoService;

    @Test
    void validateCard_withValidCardShouldReturnBinInfoResponseDTO() {
        String cardNumber = "1234567890123456789";
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO(cardNumber);
        List<BinInfo> binInfoList = new ArrayList<>();
        binInfoList.add(BinInfo.builder()
                .bin("123")
                .bankName("Bank XYZ")
                .alphaCode("ABC")
                .build());

        when(binInfoRepository.findAllByCardInRangeOrderByCreatedAsc(cardNumber)).thenReturn(Optional.of(binInfoList));

        Optional<BinInfoResponseDTO> result = binInfoService.validateCard(validationRequestDTO);

        assertTrue(result.isPresent());
        assertEquals("123", result.get().getBin());
        assertEquals("ABC", result.get().getAlphaCode());
        assertEquals("Bank XYZ", result.get().getBank());
        verify(binInfoRepository).findAllByCardInRangeOrderByCreatedAsc(cardNumber);
    }

    @Test
    void validateCard_withInvalidCardShouldReturnEmptyOptional() {
        String cardNumber = "1234567890123456789";
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO(cardNumber);
        when(binInfoRepository.findAllByCardInRangeOrderByCreatedAsc(cardNumber)).thenReturn(Optional.empty());

        Optional<BinInfoResponseDTO> result = binInfoService.validateCard(validationRequestDTO);

        assertFalse(result.isPresent());
        verify(binInfoRepository).findAllByCardInRangeOrderByCreatedAsc(cardNumber);
    }
}
