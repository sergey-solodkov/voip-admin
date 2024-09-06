package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.OptionValue;
import com.github.sergeisolodkov.voipadmin.domain.Setting;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.SettingDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Setting} and its DTO {@link SettingDTO}.
 */
@Mapper(componentModel = "spring")
public interface SettingMapper extends EntityMapper<SettingDTO, Setting> {
    @Mapping(target = "option", source = "option", qualifiedByName = "optionCode")
    @Mapping(target = "selectedValues", source = "selectedValues", qualifiedByName = "optionValueIdSet")
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    SettingDTO toDto(Setting s);

    @Mapping(target = "removeSelectedValues", ignore = true)
    Setting toEntity(SettingDTO settingDTO);

    @Named("optionCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    OptionDTO toDtoOptionCode(Option option);

    @Named("optionValueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OptionValueDTO toDtoOptionValueId(OptionValue optionValue);

    @Named("optionValueIdSet")
    default Set<OptionValueDTO> toDtoOptionValueIdSet(Set<OptionValue> optionValue) {
        return optionValue.stream().map(this::toDtoOptionValueId).collect(Collectors.toSet());
    }

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);
}
