package com.dms.planb.action.post.qna;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.RouteRegistration;
import org.boxfox.dms.utilities.database.DataBase;
import org.boxfox.dms.utilities.log.Log;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@RouteRegistration(path="/post/qna/comment", method={HttpMethod.POST})
public class UploadQnaComment implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext context) {
		DataBase database = DataBase.getInstance();
		
		int no = Integer.parseInt(context.request().getParam("no"));
		String content = context.request().getParam("content");
		String writer = context.request().getParam("writer");
		
		try {
			database.executeUpdate("INSERT INTO qna_comment(no, writer, comment_date, content) VALUES(", no, ", '", writer, "', now(), '", content, "')");
			
			context.response().setStatusCode(201).end();
			context.response().close();
		} catch(SQLException e) {
			context.response().setStatusCode(500).end();
			context.response().close();
			
			Log.l("SQLException");
		}
	}
}
