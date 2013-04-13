package es.udc.smunin.empresauriostic.ordermanager.model.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.content.Context;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Order;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Product;

/**
 * Provides different methods to parse different XML responses
 * 
 * @author Santiago Mun’n <burning1@gmail.com>
 * 
 */
public class ParsingUtils {

	private static final String SESSION_ID = "session_id";
	private static final String STATUS = "status";
	private static final String SUCCESS = "success";
	private static final String PHONE = "phone";
	private static final String EMAIL = "email";
	private static final String CITY = "city";
	private static final String TITLE = "title";
	private static final String RATING = "rating";
	private static final String PRICE = "price";
	private static final String FINISHED = "finished";
	private static final String ID = "id";
	private static final String IMAGE = "image";
	private static final String DESCRIPTION = "description";

	private static DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	public static boolean parseOperationStatus(String data) {
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(data));
			Document dom = builder.parse(is);
			return dom.getDocumentElement().getElementsByTagName(STATUS)
					.item(0).getFirstChild().getNodeValue()
					.equalsIgnoreCase(SUCCESS);
		} catch (Exception e) {
			return false;
		}
	}

	public static List<Order> parseNewOrders(String response) {
		return new ArrayList<Order>();
	}

	public static boolean parseNewOrderSubmitted(String response) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(response);
			long id = jObject.getLong("order_id");
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static List<Product> parseProducts(String response) {
		List<Product> result = new LinkedList<Product>();
		try {
			JSONObject jObject = new JSONObject(response);
			JSONArray productArray = jObject.getJSONArray("products");
			for (int i = 0; i < productArray.length(); i++) {
				JSONObject productObject = (JSONObject) productArray.get(i);
				long id = productObject.getLong("product_id");
				String name = productObject.getString("name");
				String description = productObject.getString("description");

				BigDecimal price = new BigDecimal(
						productObject.getDouble("price"));

				result.add(new Product(id, name, description, price));
				// TODO new bigdecimal(0)
			}

		} catch (Exception e) {
		}
		return result;
	}

	public static List<Order> parseOrders(Context context, String response,
			boolean recordTime) {

		List<Order> result = new LinkedList<Order>();
		try {
			JSONObject jObject = new JSONObject(response);
			JSONArray productArray = jObject.getJSONArray("orders");
			for (int i = 0; i < productArray.length(); i++) {
				JSONObject productObject = (JSONObject) productArray.get(i);
				long orderId = productObject.getLong("order_id");
				long productId = productObject.getLong("product_id");
				String productName = productObject.getString("product_name");
				int amount = productObject.getInt("amount");
				long dateOrdered = productObject.getLong("date_ordered");
				BigDecimal price = new BigDecimal(
						productObject.getDouble("order_price"));
				result.add(new Order(orderId, new Date(dateOrdered), amount,
						productName, price));

			}
			long time = jObject.getLong("server_time");
			if (result.size() > 0 && recordTime) {
				PreferencesUtil.setTime(context, time);
			}
		} catch (Exception e) {
		}
		return result;
	}
}
