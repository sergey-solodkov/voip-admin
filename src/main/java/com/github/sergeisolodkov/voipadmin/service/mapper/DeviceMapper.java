package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.Owner;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = {VoipAccountMapper.class, DeviceModelMapper.class, SettingMapper.class})
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "ownerLastName")
    @Mapping(target = "mac", source = "mac", qualifiedByName = "formatMac")
    DeviceDTO toDto(Device s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);

    @Named("deviceModelName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeviceModelDTO toDtoDeviceModelName(DeviceModel deviceModel);

    @Named("ownerLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    OwnerDTO toDtoOwnerLastName(Owner owner);

    @Mapping(source = "mac", target = "mac", qualifiedByName = "flatMac")
    Device toEntity(DeviceDTO dto);

    @Named("formatMac")
    static String formatMac(String flatMac) {
        if (flatMac.length() != 12) {
            return flatMac;
        }
        String[] octets = flatMac.split("(?<=\\G..)");
        return String
            .join(":", octets)
            .toUpperCase();
    }

    @Named("flatMac")
    static String flatMac(String formattedMac) {
        return formattedMac
            .replace(":", "")
            .replace("-", "")
            .toLowerCase();
    }
}
