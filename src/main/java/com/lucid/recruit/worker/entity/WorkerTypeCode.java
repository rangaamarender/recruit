package com.lucid.recruit.worker.entity;

public enum WorkerTypeCode {

	WORKER_W2("W2"), WORKER_C2C("C2C"), WORKER_1099("1099");

	private String code;

	WorkerTypeCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
