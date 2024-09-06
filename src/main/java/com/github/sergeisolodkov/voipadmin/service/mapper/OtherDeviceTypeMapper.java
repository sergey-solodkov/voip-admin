package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType;
import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OtherDeviceType} and its DTO {@link OtherDeviceTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface OtherDeviceTypeMapper extends EntityMapper<OtherDeviceTypeDTO, OtherDeviceType> {}
