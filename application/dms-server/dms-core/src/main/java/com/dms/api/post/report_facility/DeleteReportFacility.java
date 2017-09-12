package com.dms.api.post.report_facility;

import java.sql.SQLException;

import com.dms.account_manager.Guardian;
import com.dms.account_manager.UserManager;
import com.dms.utilities.database.DataBase;
import com.dms.utilities.log.Log;
import com.dms.utilities.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Deprecated
@Route(path="/post/report", method={HttpMethod.DELETE})
public class DeleteReportFacility implements Handler<RoutingContext> {
	private UserManager userManager;
	
	public DeleteReportFacility() {
		userManager = new UserManager();
	}
	
	@Override
	public void handle(RoutingContext context) {
		DataBase database = DataBase.getInstance();

		int no = Integer.parseInt(context.request().getParam("no"));
		
		if(!Guardian.checkParameters(no)) {
            context.response().setStatusCode(400).end();
            context.response().close();
        	return;
        }
		
		String uid = null;
		try {
			uid = userManager.getUid(userManager.getIdFromSession(context));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			database.executeUpdate("DELETE FROM facility_report WHERE no=", no);
			
			context.response().setStatusCode(200).end();
			context.response().close();
		} catch(SQLException e) {
			context.response().setStatusCode(500).end();
			context.response().close();
			
			Log.l("SQLException");
		}
	}
}
