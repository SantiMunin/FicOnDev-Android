package es.udc.smunin.empresauriostic.ordermanager.model.objectmodels;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

	private long id;
	private Date date;
	private int amount;
	private String product;
	private BigDecimal price;

	public Order(long id, Date date, int amount, String product,
			BigDecimal price) {
		super();
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.product = product;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public int getAmount() {
		return amount;
	}

	public String getProduct() {
		return product;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

}
