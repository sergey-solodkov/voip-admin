package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.VoipAccount;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.VoipAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VoipAccount} and its DTO {@link VoipAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface VoipAccountMapper extends EntityMapper<VoipAccountDTO, VoipAccount> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    VoipAccountDTO toDto(VoipAccount s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);
}
