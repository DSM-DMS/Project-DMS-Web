package com.dms.templates.rule;

import java.io.IOException;
import java.sql.SQLException;

import com.dms.account_manager.AdminManager;
import com.dms.account_manager.Guardian;
import com.dms.templates.DmsTemplate;
import com.dms.utilities.database.DataBase;
import com.dms.utilities.database.SafeResultSet;
import com.dms.utilities.log.Log;
import com.dms.utilities.routing.Route;

import freemarker.template.TemplateException;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Route(path = "/post/rule/modify", method = {HttpMethod.GET})
public class RuleModifyRouter implements Handler<RoutingContext> {
	private AdminManager adminManager;
	
	public RuleModifyRouter() {
		adminManager = new AdminManager();
	}
	
	public void handle(RoutingContext context) {
		if (!AdminManager.isAdmin(context)) return;
		DataBase database = DataBase.getInstance();
		SafeResultSet resultSet;
		
		boolean isLogin = adminManager.isLogined(context);
		if(isLogin) {
			int no = Integer.parseInt(context.request().getParam("no"));
			
			if(!Guardian.checkParameters(no)) {
	            context.response().setStatusCode(400).end();
	            context.response().close();
	        	return;
	        }
			
			DmsTemplate templates = new DmsTemplate("editor");
			
			try {
				resultSet = database.executeQuery("SELECT * FROM rule WHERE no=", no);
				resultSet.next();
				
				templates.put("category", "rule");
				templates.put("type", "modify");
				templates.put("title", resultSet.getString("title"));
				templates.put("content", resultSet.getString("content"));
				
				context.response().setStatusCode(200);
				context.response().end(templates.process());
				context.response().close();
			} catch(IOException e) {
				Log.l("IOException");
			} catch(TemplateException e) {
				Log.l("TemplateException");
			} catch(SQLException e) {
				Log.l("SQLException");
			}
		} else {
			context.response().setStatusCode(200);
            context.response().putHeader("Content-type", "text/html; utf-8");
            context.response().end("<script>window.location.href='/'</script>");
            context.response().close();
		}
	}
}