package com.sanri.test.mapstruct.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SourceMapper {
    SourceMapper INSTANCE = Mappers.getMapper(SourceMapper.class);

    @Mapping(source = "totalCount",target = "count")
    @Mapping(source = "subSource", target = "subTarget")
    Target source2target(Source source);

    @Mapping(source = "person.description", target = "description")
    @Mapping(source = "address.houseNo", target = "houseNumber")
    DeliveryAddress personAndAddressToDeliveryAddressDto(Person person, Address address);

    default SubTarget subSource2subTarget(SubSource subSource) {
        if (subSource == null) {
            return null;
        }
        SubTarget subTarget = new SubTarget();
        subTarget.setResult(!subSource.getDeleted().equals(0));
        subTarget.setName(subSource.getName()==null?"":subSource.getName()+subSource.getName());
        return subTarget;
    }

    /**
     * 更新， 使用 Address 来补全 DeliveryAddress 信息。 注意注解 @MappingTarget
     * @param address
     * @param deliveryAddress
     */
    void updateDeliveryAddressFromAddress(Address address,
                                          @MappingTarget DeliveryAddress deliveryAddress);
}