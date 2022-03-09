package com.sanri.test.mapstruct;

import com.sanri.test.mapstruct.dto.*;
import com.sanri.test.mapstruct.param.OrderQueryParam;
import com.sanri.test.mapstruct.po.Order;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParamConvertMain {
    @Test
    public void entity2queryParam() {
        Order order = new Order();
        order.setId(12345L);
        order.setOrderSn("orderSn");
        order.setOrderType(0);
        order.setReceiverKeyword("keyword");
        order.setSourceType(1);
        order.setStatus(2);
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        OrderQueryParam orderQueryParam = mapper.entity2queryParam(order);
        assertEquals(orderQueryParam.getOrderSn(), order.getOrderSn());
        assertEquals(orderQueryParam.getOrderType(), order.getOrderType());
        assertEquals(orderQueryParam.getReceiverKeyword(), order.getReceiverKeyword());
        assertEquals(orderQueryParam.getSourceType(), order.getSourceType());
        assertEquals(orderQueryParam.getStatus(), order.getStatus());
    }

    @Test
    public void testNotEquals(){
        Source source = new Source();
        source.setId(UUID.randomUUID().toString());
        source.setNum(20);
        source.setTotalCount(101);

        SubSource subSource = new SubSource();
        subSource.setDeleted(1);
        subSource.setName("9420");
        source.setSubSource(subSource);

        Target target = SourceMapper.INSTANCE.source2target(source);
        System.out.println(target);
    }

    @Test
    public void testMerge(){
        Person person = new Person();
        person.setFirstName("first");
        person.setDescription("9420");
        person.setHeight(170);
        person.setLastName("9420");

        Address address = new Address();
        address.setDescription("address desc ");
        address.setHouseNo(29);
        address.setStreet("street ");
        address.setZipCode(344);

        DeliveryAddress deliveryAddress = SourceMapper.INSTANCE.personAndAddressToDeliveryAddressDto(person, address);
        System.out.println(deliveryAddress);

        // 如果其中一个为 null ,有值的部分会为你匹配上
        deliveryAddress = SourceMapper.INSTANCE.personAndAddressToDeliveryAddressDto(person, null);
        System.out.println(deliveryAddress);
    }

    @Test
    public void testUpdateBean(){
        Person person = new Person();
        person.setFirstName("first");
        person.setDescription("perSonDescription");
        person.setHeight(183);
        person.setLastName("homejim");
        DeliveryAddress deliveryAddress = SourceMapper.INSTANCE.personAndAddressToDeliveryAddressDto(person,null);
        assertEquals(deliveryAddress.getFirstName(), person.getFirstName());
        assertNull(deliveryAddress.getStreet());

        Address address = new Address();
        address.setDescription("addressDescription");
        address.setHouseNo(29);
//        address.setStreet("street");
        address.setZipCode(344);
        // 空数据会覆盖原来数据
        SourceMapper.INSTANCE.updateDeliveryAddressFromAddress(address, deliveryAddress);

        System.out.println(deliveryAddress);
    }

    @Test
    public void testPropertyOverwrite(){
        Person person = new Person();
        person.setFirstName("first");
        person.setDescription("perSonDescription");
        person.setHeight(183);
        person.setLastName("homejim");

        Student student = new Student();
        student.setFirstName("sanri");
        student.setLastName("9420");
        StudentMerge studentMerge = OtherMapper.INSTANCE.studentMerge(person, student);

        System.out.println(studentMerge);
    }
}
