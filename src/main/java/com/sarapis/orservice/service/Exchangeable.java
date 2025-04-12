package com.sarapis.orservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

public interface Exchangeable {
    long writeCsv(ZipOutputStream zipOutputStream) throws IOException;
    long writePdf(ZipOutputStream zipOutputStream) throws IOException;
    void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException;
}
