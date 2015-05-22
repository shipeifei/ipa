/**
 * file com.ipassistat.ipa.domain.bean
 * create at 2015-5-21
 * author 时培飞
 * 
 */
package com.ipassistat.ipa.domain.bean;

/**
 * 打电话实体
 * 
 * @author shipeifei
 * 
 */
public class CallDomainResponse extends DomainBaseResponse {

	public class Semantic {

		private CallIntent intent;

		/**
		 * @return the intent
		 */
		public CallIntent getIntent() {
			return intent;
		}

		/**
		 * @param intent
		 *            the intent to set
		 */
		public void setIntent(CallIntent intent) {
			this.intent = intent;
		}
	}
	
	private Semantic semantic;

	/**
	 * @return the semantic
	 */
	public Semantic getSemantic() {
		return semantic;
	}

	/**
	 * @param semantic the semantic to set
	 */
	public void setSemantic(Semantic semantic) {
		this.semantic = semantic;
	}

	/*
	 * public class () {
	 * 
	 * }
	 */

}
