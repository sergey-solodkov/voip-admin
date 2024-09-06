package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.VendorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorDTO}.
 */
@Mapper(componentModel = "spring")
public interface VendorMapper extends EntityMapper<VendorDTO, Vendor> {
    @Mapping(target = "options", source = "options", qualifiedByName = "optionCodeSet")
    VendorDTO toDto(Vendor s);

    @Mapping(target = "options", ignore = true)
    @Mapping(target = "removeOptions", ignore = true)
    Vendor toEntity(VendorDTO vendorDTO);

    @Named("optionCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    OptionDTO toDtoOptionCode(Option option);

    @Named("optionCodeSet")
    default Set<OptionDTO> toDtoOptionCodeSet(Set<Option> option) {
        return option.stream().map(this::toDtoOptionCode).collect(Collectors.toSet());
    }
}
