package com.lookup.service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookup.service.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final ObjectMapper objectMapper;

    @Override
    public <T> List<T> fetchBeansFromZipFile(TypeReference<List<T>> typeReference,
                                             String filePath,
                                             String fileName
    ) {
        try (InputStream inputStream = new URL(filePath).openStream();
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().equals(fileName)) {
                    return objectMapper.readValue(zipInputStream, typeReference);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while fetching bean information from the ZIP file: {}", e.getMessage());
        }
        return Collections.emptyList();
    }
}
