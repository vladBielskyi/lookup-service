package com.lookup.service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lookup.service.dto.request.ValidationRequestDTO;
import com.lookup.service.dto.response.BinInfoResponseDTO;
import com.lookup.service.entity.BinInfo;
import com.lookup.service.repository.BinInfoRepository;
import com.lookup.service.service.BinInfoService;
import com.lookup.service.service.FileService;
import com.lookup.service.util.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinInfoServiceImpl implements BinInfoService {

    private final FileService fileService;
    private final BinInfoRepository binInfoRepository;
    private final ExecutorService executorService;

    private static final Integer CARD_RANGE_SIZE = 19;
    private static final String ZERO_APPENDER = "0";
    private static final Integer BATCH_SIZE = 1000;
    private static final Integer AWAIT_TIME_MINUTES = 7;

    @Value("${lookup.service.bin.info.file.path}")
    private String binInfoFilePath;
    @Value("${lookup.service.bin.info.file.name}")
    private String binInfoFileName;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<BinInfoResponseDTO> validateCard(ValidationRequestDTO validationRequestDTO) {
        StringBuilder cardBuilder = new StringBuilder(validationRequestDTO.getCard());
        padCardNumber(cardBuilder);

        String cardNumber = cardBuilder.toString();
        Optional<List<BinInfo>> binInfo = binInfoRepository.findAllByCardInRangeOrderByCreatedAsc(cardNumber);
        if (!binInfo.isPresent() || binInfo.get().isEmpty()) {
            return Optional.empty();
        }

        return binInfo.map(x -> mapToResponseDTO(x.get(0))); // return oldest record
    }


    @Override
    @Transactional
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES) // run every 60 minutes
    public void importBinInfosFromZip() {
        log.info("Starting import BinInfos");
        List<BinInfo> incomeBinInfos = fileService.fetchBeansFromZipFile(
                new TypeReference<List<BinInfo>>() {},
                binInfoFilePath,
                binInfoFileName
        );

        if (incomeBinInfos.isEmpty()) {
            log.warn("No bin infos found in the ZIP file. Skipping import.");
            return;
        }
        log.info("Found {} bin infos in the ZIP file. Saving to the database.", incomeBinInfos.size());

        List<Runnable> runnable = buildBatchedRunnable(incomeBinInfos, BATCH_SIZE);
        CountDownLatch latch = new CountDownLatch(runnable.size());

        runnable.forEach(task -> executorService.submit(() -> {
            task.run();
            latch.countDown();
        }));

        try {
            latch.await(AWAIT_TIME_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime createdThreshold = LocalDateTime.now().minusMinutes(60);
        binInfoRepository.deleteAllByCreated(createdThreshold); // Delete old data
        log.info("Successfully imported bin infos from the ZIP file.");
    }

    //////////////////////////////////// UTIL METHOD ///////////////////////////////////////////////////////////////////

    /**
     * Pads the card number with zeros at the end until its length reaches CARD_RANGE_SIZE.
     *
     * @param cardBuilder The StringBuilder containing the card number.
     */
    private void padCardNumber(StringBuilder cardBuilder) {
        int cardLength = cardBuilder.length();
        if (cardLength < CARD_RANGE_SIZE) {
            cardBuilder.append(ZERO_APPENDER.repeat(CARD_RANGE_SIZE - cardLength));
        }
    }

    /**
     * Maps a BinInfo entity to a BinInfoResponseDTO.
     *
     * @param binInfo The BinInfo entity to map.
     * @return The mapped BinInfoResponseDTO.
     */
    private BinInfoResponseDTO mapToResponseDTO(BinInfo binInfo) {
        return BinInfoResponseDTO.builder()
                .bin(binInfo.getBin())
                .alphaCode(binInfo.getAlphaCode())
                .bank(binInfo.getBankName())
                .build();
    }

    /**
     * Saves a batch of BinInfo objects to the repository.
     *
     * @param batch The list of BinInfo objects to be saved.
     * @return The saved BinInfo objects as an Iterable.
     */
    private Iterable<BinInfo> saveBatchBinInfos(List<BinInfo> batch) {
        return binInfoRepository.saveAll(batch);
    }

    /**
     * Builds a list of batched runnable tasks based on the provided bin info list and batch size.
     *
     * @param binInfoList the list of bin infos to be processed
     * @param batchSize   the size of each batch
     * @return the list of batched runnable tasks
     */
    private List<Runnable> buildBatchedRunnable(List<BinInfo> binInfoList, Integer batchSize) {
        return ListUtil.createSubList(binInfoList, batchSize).stream()
                .map(sublist -> (Runnable) () -> {
                    saveBatchBinInfos(sublist);
                    log.info("batch saved [{}]", sublist.size());
                })
                .collect(Collectors.toList());
    }
}
