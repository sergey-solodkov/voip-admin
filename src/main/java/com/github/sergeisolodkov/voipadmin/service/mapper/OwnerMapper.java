package com.github.sergeisolodkov.voipadmin.service.mapper;

import com.github.sergeisolodkov.voipadmin.domain.Department;
import com.github.sergeisolodkov.voipadmin.domain.Owner;
import com.github.sergeisolodkov.voipadmin.service.dto.DepartmentDTO;
import com.github.sergeisolodkov.voipadmin.service.dto.OwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Owner} and its DTO {@link OwnerDTO}.
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper extends EntityMapper<OwnerDTO, Owner> {
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
    OwnerDTO toDto(Owner s);

    @Named("departmentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoDepartmentName(Department department);
}
