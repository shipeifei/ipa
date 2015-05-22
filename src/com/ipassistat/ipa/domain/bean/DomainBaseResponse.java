package com.ipassistat.ipa.domain.bean;

/***
 * 所有领域的基类
 * 
 * @author shipeifei
 * 
 */
public class DomainBaseResponse {

	public class ResponseError {
		/***
		 * 自定义错误编码
		 */
		private String code = "";
		/***
		 * 错误信息
		 */
		private String message = "";

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code
		 *            the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}

	/***
	 * 服务器返回应答码 0:操作成功 1:业务操作失败 2：无效请求 3:服务器内部错误 4:应用不支持此功能模块 5:服务器不理解或者不能处理此文本
	 */
	private int rc;

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	/***
	 * 服务的全局唯一名称
	 */
	private String service = "";

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	/***
	 * 服务器返回的操作行为，主要用来区分不同的操作动作
	 */
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/***
	 * 用户的输入,可能和请求中的原始 text 不完全一致,因 服务器可能会对 text 做语音纠错
	 */
	private String text = "";

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the history
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(String history) {
		this.history = history;
	}

	/**
	 * @return the error
	 */
	public ResponseError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ResponseError error) {
		this.error = error;
	}

	/***
	 * 历史记录
	 */
	private String history = "";
	
	/***
	 * 错误信息
	 */
	private ResponseError error=null;
}
