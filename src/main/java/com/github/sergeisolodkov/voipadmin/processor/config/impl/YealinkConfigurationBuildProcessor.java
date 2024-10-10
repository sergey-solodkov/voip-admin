package com.github.sergeisolodkov.voipadmin.processor.config.impl;

import com.github.sergeisolodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.OptionValueType;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import com.github.sergeisolodkov.voipadmin.processor.attr.PlainTextConfigAttrBuilder;
import com.github.sergeisolodkov.voipadmin.processor.config.ConfigurationBuildProcessor;
import com.github.sergeisolodkov.voipadmin.processor.domain.FileExtension;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;

import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog.CONFIG_TEMPLATE;
import static java.util.Objects.nonNull;

@Component("yealink")
@RequiredArgsConstructor
// TODO old implementation, try to improve
public class YealinkConfigurationBuildProcessor implements ConfigurationBuildProcessor {

    private static final String SECTION_TITLE_TEMPLATE = "##{0}{1}{2}##";
    private static final int SECTION_TITLE_LINE_LENGTH = 83;

    private final FileStorage fileStorage;

    @Override
    public ConfigurationFile process(Device device) throws IOException {
        var configTemplatePath = Optional
            .ofNullable(device.getModel().getConfigTemplatePath())
            .orElseThrow(() -> new IllegalArgumentException("Device model has no configuration template"));

        var path = Path.of(configTemplatePath);
        var configTemplate = fileStorage.download(StorageType.S3, CONFIG_TEMPLATE, path);

        var fileName = buildFileName(device, FileExtension.CFG);
        var content = buildContent(configTemplate, device);

        return ConfigurationFile.builder()
            .fileName(fileName)
            .fileContent(content)
            .build();
    }

    private byte[] buildContent(Resource templateResource, Device device) throws IOException {
        var configTemplate = new String(
            templateResource.getContentAsByteArray(),
            StandardCharsets.UTF_8
        );

        PlainTextConfigAttrBuilder attrBuilder = new PlainTextConfigAttrBuilder(configTemplate);
        // Main settings
        attrBuilder
            .set("static.security.user_name.var", device.getWebAccessLogin())
            .set("static.security.user_password", device.getWebAccessPasswordHash()) // TODO hash
            .set("static.network.internet_port.type", device.getDhcpEnabled() ? "0" : "2")
            .set("local_time.ntp_server1", device.getNtp());

        if (!device.getDhcpEnabled()) {
            attrBuilder
                .set("static.network.internet_port.ip", device.getIpAddress())
                .set("static.network.internet_port.mask", device.getSubnetMask())
                .set("static.network.internet_port.gateway", device.getDefaultGw())
                .set("static.network.primary_dns", device.getDns1())
                .set("static.network.secondary_dns", device.getDns2());
        }

        if (nonNull(device.getProvisioningUrl())) {
            String provisioningUrl = MessageFormat.format(
                "{0}://{1}",
                device.getProvisioningMode().toString().toLowerCase(),
                device.getProvisioningUrl()
            );
            attrBuilder
                .set("static.auto_provision.server.url", provisioningUrl);
        }

        attrBuilder.addNewLine();
        attrBuilder.addNewLine();

        addSectionTitle(attrBuilder, "Below sections were inserted using VoIP-Admin");
        attrBuilder.addNewLine();

        // Line settings
        if (nonNull(device.getVoipAccounts()) && !device.getVoipAccounts().isEmpty()) {
            device.getVoipAccounts().forEach(voipAccount -> {
                attrBuilder.remove("account." + voipAccount.getLineNumber());
                attrBuilder.addNewLine();
                addSectionTitle(attrBuilder, "Account " + voipAccount.getLineNumber() + " settings");
                attrBuilder.addNewLine();

                attrBuilder.add("account." + voipAccount.getLineNumber() + ".enable",voipAccount.getLineEnable() ? "1" : "0");
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".label", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".display_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".auth_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".user_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".password", voipAccount.getPasswordHash()); // TODO hash
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".sip_server.1.address", voipAccount.getSipServer());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".sip_server.1.port", voipAccount.getSipPort());

            });
        }

        // Additional settings
        if (nonNull(device.getSettings()) && !device.getSettings().isEmpty()) {
            device.getSettings().forEach(setting -> {
                Object value = setting.getOption().getValueType().equals(OptionValueType.SELECT)
                    ? setting.getSelectedValues().iterator().next().getValue()
                    : setting.getTextValue();
                attrBuilder.set(setting.getOption().getCode(), value);
            });
        }

        return attrBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void addSectionTitle(PlainTextConfigAttrBuilder attrBuilder, String sectionTitle) {
        attrBuilder
            .addComment(StringUtils.repeat("#", SECTION_TITLE_LINE_LENGTH))
            .addComment(
                MessageFormat.format(
                    SECTION_TITLE_TEMPLATE,
                    StringUtils.repeat(" ", (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 2),
                    sectionTitle,
                    StringUtils.repeat(" ", sectionTitle.length() % 2 != 0 ?
                        (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 2 : (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 1)
                )
            )
            .addComment(StringUtils.repeat("#", SECTION_TITLE_LINE_LENGTH));
    }
}
