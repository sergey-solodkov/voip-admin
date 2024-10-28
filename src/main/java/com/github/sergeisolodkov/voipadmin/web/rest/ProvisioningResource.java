package com.github.sergeisolodkov.voipadmin.web.rest;

import com.github.sergeisolodkov.voipadmin.service.ProvisioningService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/provisioning")
@RequiredArgsConstructor
@Slf4j
public class ProvisioningResource {

    private final ProvisioningService provisioningService;

    @GetMapping("/config/{mac}")
    public ResponseEntity<Resource> getConfigForMac(@PathVariable String mac, HttpServletRequest request) throws IOException {
        log.debug("REST request from {} to download config file for mac : {}", request.getRemoteAddr(), mac);
        var resource = provisioningService.configForMac(mac);

        var contentDisposition = ContentDisposition.builder("attachment")
            .filename(resource.getFilename())
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .body(resource);
    }
}
