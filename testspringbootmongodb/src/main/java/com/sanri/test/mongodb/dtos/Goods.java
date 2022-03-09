package com.sanri.test.mongodb.dtos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "goods")
public class Goods {
	@Id
	private Integer id;
	private String name;
	private String brand;
	private Float price;
	private Long stock;
}
