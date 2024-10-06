package com.github.sergeisolodkov.voipadmin.domain.enumeration;

import java.util.Set;

/**
 * The ProvisioningMode enumeration.
 */
public enum ProvisioningMode {
    FTP,
    TFTP,
    HTTP,
    HTTPS;

    public static Set<ProvisioningMode> FILE_TRANSFER_PROTOCOLS = Set.of(FTP, TFTP);
}
