package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.OptionValue;
import com.github.sergeisolodkov.voipadmin.domain.Setting;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.SettingDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OptionValue} and its DTO {@link OptionValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface OptionValueMapper extends EntityMapper<OptionValueDTO, OptionValue> {
    @Mapping(target = "settings", source = "settings", qualifiedByName = "settingIdSet")
    @Mapping(target = "option", source = "option", qualifiedByName = "optionId")
    OptionValueDTO toDto(OptionValue s);

    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "removeSettings", ignore = true)
    OptionValue toEntity(OptionValueDTO optionValueDTO);

    @Named("settingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SettingDTO toDtoSettingId(Setting setting);

    @Named("settingIdSet")
    default Set<SettingDTO> toDtoSettingIdSet(Set<Setting> setting) {
        return setting.stream().map(this::toDtoSettingId).collect(Collectors.toSet());
    }

    @Named("optionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OptionDTO toDtoOptionId(Option option);
}
