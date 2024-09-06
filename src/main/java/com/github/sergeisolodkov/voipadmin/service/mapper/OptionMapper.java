package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.VendorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Option} and its DTO {@link OptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OptionMapper extends EntityMapper<OptionDTO, Option> {
    @Mapping(target = "vendors", source = "vendors", qualifiedByName = "vendorNameSet")
    @Mapping(target = "models", source = "models", qualifiedByName = "deviceModelNameSet")
    OptionDTO toDto(Option s);

    @Mapping(target = "removeVendors", ignore = true)
    @Mapping(target = "models", ignore = true)
    @Mapping(target = "removeModels", ignore = true)
    Option toEntity(OptionDTO optionDTO);

    @Named("vendorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VendorDTO toDtoVendorName(Vendor vendor);

    @Named("vendorNameSet")
    default Set<VendorDTO> toDtoVendorNameSet(Set<Vendor> vendor) {
        return vendor.stream().map(this::toDtoVendorName).collect(Collectors.toSet());
    }

    @Named("deviceModelName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeviceModelDTO toDtoDeviceModelName(DeviceModel deviceModel);

    @Named("deviceModelNameSet")
    default Set<DeviceModelDTO> toDtoDeviceModelNameSet(Set<DeviceModel> deviceModel) {
        return deviceModel.stream().map(this::toDtoDeviceModelName).collect(Collectors.toSet());
    }
}
