package es.udc.smunin.empresauriostic.ordermanager.model.objectmodels;

import java.math.BigDecimal;

public class Product {
	private long id;
	private String name;
	private String description;
	private BigDecimal price;

	public Product(long id, String name, String description, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}


}
