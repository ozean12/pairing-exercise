package io.billie.shipmentnotifier.model.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public class JpaBaseEntity implements Serializable {

	private static final long serialVersionUID = 9147304262683573000L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime lastModifiedAt;

	private LocalDateTime createdAt;

}
