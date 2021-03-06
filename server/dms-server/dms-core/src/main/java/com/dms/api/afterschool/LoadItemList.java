package com.dms.api.afterschool;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dms.utilities.database.DB;
import com.dms.utilities.log.Log;
import com.dms.utilities.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Route(path = "/afterschool/item/list", method = { HttpMethod.GET })
public class LoadItemList implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		try {
			ResultSet rs = DB.executeQuery("SELECT * FROM afterschool_items");
			JSONArray response = new JSONArray();

			while (rs.next()) {
				JSONObject afterschool = new JSONObject();

				afterschool.put("no", rs.getInt("no"));
				afterschool.put("title", rs.getString("title"));
				afterschool.put("target", rs.getInt("target"));
				afterschool.put("on_monday", rs.getBoolean("on_monday"));
				afterschool.put("on_tuesday", rs.getBoolean("on_tuesday"));
				afterschool.put("on_saturday", rs.getBoolean("on_saturday"));

				response.put(afterschool);
			}
			
			if(response.length() != 0) {
				ctx.response().setStatusCode(200);
				ctx.response().end(response.toString());
				ctx.response().close();
			} else {
				ctx.response().setStatusCode(204).end();
				ctx.response().close();
			}
		} catch (SQLException e) {
			Log.l("SQLException");
		}
	}
}
