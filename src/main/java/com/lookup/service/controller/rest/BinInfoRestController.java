package com.lookup.service.controller.rest;

import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;
import com.lookup.service.service.BinInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/infos")
@RequiredArgsConstructor
public class BinInfoRestController {

    private final BinInfoService binInfoService;

    /**
     * Validates a card based on the provided request.
     *
     * @param validationRequestDTO The request containing the card information.
     * @return ResponseEntity containing the validation response.
     */
    @PostMapping("/validate-card")
    public ResponseEntity<BinInfoResponseDTO> validateCard(
            @RequestBody ValidationRequestDTO validationRequestDTO
    ) {
        Optional<BinInfoResponseDTO> validation = binInfoService.validateCard(validationRequestDTO);

        if (!validation.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(validation.get());
    }
}
