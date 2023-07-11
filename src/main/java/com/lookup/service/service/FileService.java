package com.lookup.service.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public interface FileService {

    /**
     * Fetches the bean information from the ZIP file.
     *
     * @param typeReference
     * @param filePath
     * @param fileName
     * @return a list of objects representing the extracted data
     */
    <T> List<T> fetchBeansFromZipFile(TypeReference<List<T>> typeReference, String filePath, String fileName);
}
