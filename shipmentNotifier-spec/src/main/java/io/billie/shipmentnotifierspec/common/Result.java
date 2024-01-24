package io.billie.shipmentnotifierspec.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Result implements Serializable {

	private static final long serialVersionUID = 6091567334208093240L;

	private ResultStatus title;

	private int status;

	private String message;

	private ResultLevel level;

	public Result(ResultStatus title) {
		this.title = title;
		this.status = title.getStatus();
		this.message = title.getDescription();
		this.level = getResultLevel(title);
	}

	public Result(ResultStatus title, ResultLevel level) {
		this.title = title;
		this.status = title.getStatus();
		this.message = title.getDescription();
		this.level = level;
	}

	public ResultLevel getResultLevel(ResultStatus resultStatus) {
		if (resultStatus == ResultStatus.SUCCESS) {
			return ResultLevel.INFO;
		} else if (resultStatus == ResultStatus.FAILURE) {
			return ResultLevel.BLOCKER;
		} else {
			return ResultLevel.WARN;
		}
	}
}