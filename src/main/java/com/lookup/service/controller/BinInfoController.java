package com.lookup.service.controller;

import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;
import com.lookup.service.service.BinInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/infos")
@RequiredArgsConstructor
public class BinInfoController {
    
    private final BinInfoService binInfoService;

    private static final String BIN_INFO_PAGE = "bin-info";

    /**
     * Retrieves the information page.
     *
     * @return the view name for the information page
     */
    @GetMapping
    public String getInfosPage() {
        return BIN_INFO_PAGE;
    }

    /**
     * Validates a card based on the provided search parameter.
     *
     * @param searchField the search parameter representing the card
     * @return the view name for the information page
     */
    @PostMapping("/validate-card")
    public String validateCard(
            @RequestParam("searchField") String searchField,
            Model model
    ) {
        Optional<BinInfoResponseDTO> binInfo = binInfoService.validateCard(new ValidationRequestDTO(searchField));
        model.addAttribute("binInfo", binInfo.orElse(null));
        return BIN_INFO_PAGE;
    }
}

