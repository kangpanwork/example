package com.sanri.test.mapstruct.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OtherMapper {
    OtherMapper INSTANCE = Mappers.getMapper(OtherMapper.class);

    /**
     * 如果有相同字段，需要明确指定，并不是后面覆盖前面的
     * @param person
     * @param student
     * @return
     */
    @Mappings({
            @Mapping(target = "firstName",ignore = true),
            @Mapping(target = "lastName",source = "student.lastName")
    })
    StudentMerge studentMerge(Person person, Student student);
}
