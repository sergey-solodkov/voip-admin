package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.VendorDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link DeviceModel} and its DTO {@link DeviceModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceModelMapper extends EntityMapper<DeviceModelDTO, DeviceModel> {
    @Mapping(target = "otherDeviceType", source = "otherDeviceType", qualifiedByName = "otherDeviceTypeId")
    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "vendorName")
    @Mapping(target = "options", source = "options", qualifiedByName = "optionCodeSet")
    @Mapping(target = "configTemplateFileName", source = "configTemplatePath", qualifiedByName = "pathToFileName")
    @Mapping(target = "firmwareFileName", source = "firmwareFilePath", qualifiedByName = "pathToFileName")
    DeviceModelDTO toDto(DeviceModel s);

    @Mapping(target = "removeOptions", ignore = true)
    @Mapping(target = "configTemplatePath", ignore = true)
    @Mapping(target = "firmwareFilePath", ignore = true)
    DeviceModel toEntity(DeviceModelDTO deviceModelDTO);

    @Named("otherDeviceTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OtherDeviceTypeDTO toDtoOtherDeviceTypeId(OtherDeviceType otherDeviceType);

    @Named("vendorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VendorDTO toDtoVendorName(Vendor vendor);

    @Named("optionCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    OptionDTO toDtoOptionCode(Option option);

    @Named("optionCodeSet")
    default Set<OptionDTO> toDtoOptionCodeSet(Set<Option> option) {
        return option.stream().map(this::toDtoOptionCode).collect(Collectors.toSet());
    }

    @Named("pathToFileName")
    default String pathToFileName(String path) {
        return Optional.ofNullable(path)
            .map(value -> Paths.get(path).getFileName())
            .map(Path::toString)
            .orElse(null);
    }
}
