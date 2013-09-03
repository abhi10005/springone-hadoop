/*
 * Copyright 2011-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springdeveloper.hadoop.hive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class HiveJdbcApp {

	private static final Log log = LogFactory.getLog(HiveJdbcApp.class);

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/hive-jdbc-context.xml", HiveJdbcApp.class);
		log.info("Hive JDBC Application Running");
		context.registerShutdownHook();
		
		String tableDdl = "create external table if not exists tweetdata (value STRING) LOCATION '/tweets/input'";
		String query = "select r.retweetedUser, '\t', count(r.retweetedUser) as count " +
					" from tweetdata j " +
					" lateral view json_tuple(j.value, 'retweet', 'retweetedStatus') t as retweet, retweetedStatus " + 
					" lateral view json_tuple(t.retweetedStatus, 'fromUser') r as retweetedUser " +
					" where t.retweet = 'true' " +
					" group by r.retweetedUser order by count desc limit 10";
		String results = "insert overwrite directory '/tweets/hiveout'";
		
		JdbcTemplate template = context.getBean(JdbcTemplate.class);
		template.execute(tableDdl);

		template.execute(results + " " + query);
		
		context.close();
	}
}
