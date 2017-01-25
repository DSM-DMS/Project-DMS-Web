package com.dms.planb.core;

/*
 * Communication : HTTP Protocol, POST method, JSON exchange
 * 
 * Action Request : Command in header, reference Commands class
 * Request Data : JSON
 */

import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.dms.planb.support.ActionPerformer;
import org.boxfox.dms.utilities.Log;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

class DmsVerticle extends AbstractVerticle {
	private HttpServer server;
	private HttpServerResponse response;
	
	private JSONObject requestObject;
	private JSONObject responseObject;
	// org.json.simple.JSONObject

	/*
	 * (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {
		Log.l("Server started");
		// org.boxfox.dms.utilities.Log
		
		server = vertx.createHttpServer();
		server.requestHandler(request -> {
			Log.l("Received request : " + request.host());
			
			Buffer totalBuffer = Buffer.buffer();
//			MultiMap params = request.params();
			
			if(request.method() == HttpMethod.POST) {
				// The server will only work if the Http method is POST.
				Log.l("Received POST method : " + request.host());
				Log.l("Headers : " + request.headers());
				
				request.handler(buffer -> {
					totalBuffer.appendBuffer(buffer);
				});
				
				request.endHandler(v -> {
					 // The endHandler of the request is invoked when the entire request, including any body has been fully read.
					Log.l("Fully read : " + totalBuffer);
					
					// 1-1. Get command from header.
					int command = Integer.parseInt(request.getHeader("command"));
					
					// 1-2. Get client's info
						JSONObject clientObject = (JSONObject)JSONValue.parse(request.getHeader("User-Agent"));
//						clientObject.getString("Version");
//						clientObject.getString("UUID");
					
					// 1-3. Get request object from buffer.
						requestObject = (JSONObject)JSONValue.parse(totalBuffer.toString());
//						requestObject = new JSONObject().put("testKey", "testValue");
					
					/*
					 *  2. Performs the operation.
					 *  Branch off the ActionPerformer class' perform method.
					 */
					try {
						responseObject = ActionPerformer.perform(command, requestObject);
					} catch (SQLException e) {
						Log.l("SQLException");
					}
					
					// 3. Response to client.
					response = request.response();
					response.putHeader("Content-type", "application/json; charset=utf-8");
					
					response.setStatusCode((int)responseObject.get("status"));
					// Success : 1, Fail : 2
					Log.l("Responsed status code : " + responseObject.get("status"));
					responseObject.remove("status");
					
					response.end(responseObject.toString());
					Log.l("Responsed object : " + responseObject.toString());
					response.close();
				}); // endHandler
			} else {
				/*
				 *  If Http method is CONNECT, DELETE, GET, HEAD, OPTIONS, or other ...
				 *  Communication fail.
				 */
			}
		}).listen(10419);
	}
	
	public void stop(@SuppressWarnings("rawtypes") Future stopFuture) throws Exception {
		
	}
}
