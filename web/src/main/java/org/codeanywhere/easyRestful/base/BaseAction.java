package org.codeanywhere.easyRestful.base;

import org.codeanywhere.easyRestful.base.annotation.Request;

public class BaseAction implements Action {
	@Request
	private RequestContext context;

	public void execute() {
	}

	public void list(String start, String end) {

	}

	public void detail(String id) {

	}

	public RequestContext getRequestContext() {
		return this.context;
	}

}
