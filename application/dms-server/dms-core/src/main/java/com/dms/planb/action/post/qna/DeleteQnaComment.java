package com.dms.planb.action.post.qna;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.DELETE_QNA_COMMENT)
public class DeleteQnaComment implements Actionable {
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		int no = requestObject.getInt("no");
		String date = requestObject.getString("date");
		
		int status = database.executeUpdate("DELETE qna_comment WHERE no=", no, " AND date='", date, "'");
		
		responseObject.put("status", status);
		
		return responseObject;
	}
}