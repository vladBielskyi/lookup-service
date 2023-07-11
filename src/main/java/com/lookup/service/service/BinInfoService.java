package com.lookup.service.service;

import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;

import java.util.Optional;

public interface BinInfoService {

    /**
     * Validates a card using the provided validation request DTO.
     *
     * @param validationRequestDTO the validation request DTO containing card information
     * @return a {@link Optional<BinInfoResponseDTO>} with the validation result and bin information
     */
    Optional<BinInfoResponseDTO> validateCard(ValidationRequestDTO validationRequestDTO);

    /**
     * This method is scheduled to run periodically.
     * It reads the BIN information from the ZIP file, saves it to the database.
     */
    void importBinInfosFromZip();
}
