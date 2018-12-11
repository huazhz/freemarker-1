package com.golaxy.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.golaxy.model.WordConfiguration;

public class JDBCUtil {
	private static WordConfiguration config = WordConfiguration.getInstance();

	public static String getTaskName(Long taskId) {
		try {
			String sql = "select taskName from topic_task where id=?";
			PreparedStatement prepar = config.getConn().prepareStatement(sql);
			prepar.setLong(1, taskId);
			ResultSet ex = prepar.executeQuery();
			if (ex.next()) {
				String topicName = ex.getString("taskName");
				return topicName;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

}
