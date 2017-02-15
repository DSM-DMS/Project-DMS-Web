package com.dms.planb.action.selectaction;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.database.SafeResultSet;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command=Commands.LOAD_GOINGOUT_STATUS)
public class LoadGoingoutStatus implements Actionable {
	@Override
	public EasyJsonObject action(int command, EasyJsonObject requestObject) throws SQLException {
		String id = requestObject.getString("id");
		
		SafeResultSet resultSet = database.executeQuery("SELECT * FROM goingout_apply WHERE id='", id, "'");
		
		if(resultSet.next()) {
			responseObject.put("dept_day", resultSet.getString("dept_day"));
			responseObject.put("reason", resultSet.getString("reason"));
		} else {
			responseObject.put("status", 404);
		}
		
		return responseObject;
	}
}