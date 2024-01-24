package io.billie.shipmentnotifierspec.response;

import io.billie.shipmentnotifierspec.common.ResponseService;
import io.billie.shipmentnotifierspec.common.Result;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import lombok.ToString;

@ToString(callSuper = true)
public class GeneralResponse extends ResponseService {

	@Deprecated
	public GeneralResponse() {
		setResult(ResultStatus.SUCCESS);
	}

	public GeneralResponse(ResultStatus resultStatus) {
		setResult(resultStatus);
	}

	public GeneralResponse(Result result) {
		setResult(result);
	}

	public static GeneralResponse success() {
		return of(ResultStatus.SUCCESS);
	}

	public static GeneralResponse of(ResultStatus resultStatus) {
		return new GeneralResponse(resultStatus);
	}

}
