package io.billie.shipmentnotifierspec.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseService {

	private Result result;

	@JsonProperty
	public void setResult(Result result) {
		this.result = result;
	}

	public void setResult(ResultStatus resultStatus) {
		if (resultStatus == null) {
			return;
		}
		Result result = new Result();
		result.setTitle(resultStatus);
		result.setMessage(resultStatus.getDescription());
		result.setStatus(resultStatus.getStatus());
		result.setLevel(result.getResultLevel(resultStatus));
		this.result = result;
	}

	public void setResult(ResultStatus resultStatus, String message) {
		Result result = new Result();
		result.setTitle(resultStatus);
		result.setMessage(message);
		result.setStatus(resultStatus.getStatus());
		result.setLevel(result.getResultLevel(resultStatus));
		this.result = result;
	}

	public void setResult(ResultStatus resultStatus, boolean isExternal) {
		if (resultStatus == null) {
			return;
		}
		Result result = new Result();
		result.setMessage(resultStatus.getDescription());
		result.setStatus(resultStatus.getStatus());
		if (!isExternal) {
			result.setTitle(resultStatus);
		}
		result.setLevel(result.getResultLevel(resultStatus));
		this.result = result;
	}

}
