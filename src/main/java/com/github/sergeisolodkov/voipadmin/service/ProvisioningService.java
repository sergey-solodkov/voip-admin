package com.github.sergeisolodkov.voipadmin.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ProvisioningService {

    Resource configForMac(String mac) throws IOException;
}
