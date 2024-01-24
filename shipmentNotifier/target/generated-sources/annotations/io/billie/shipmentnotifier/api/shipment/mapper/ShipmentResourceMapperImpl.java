package io.billie.shipmentnotifier.api.shipment.mapper;

import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;
import io.billie.shipmentnotifierspec.request.ShipmentRequest;
import io.billie.shipmentnotifierspec.response.ShipmentResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-24T17:39:52+0330",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Private Build)"
)
@Component
public class ShipmentResourceMapperImpl implements ShipmentResourceMapper {

    @Override
    public ShipmentDto toShipmentModel(ShipmentRequest request) {
        if ( request == null ) {
            return null;
        }

        ShipmentDto shipmentDto = new ShipmentDto();

        shipmentDto.setOrderTrackingCode( request.getOrderTrackingCode() );
        shipmentDto.setShipmentAmount( request.getShipmentAmount() );
        shipmentDto.setMerchantNumber( request.getMerchantNumber() );

        return shipmentDto;
    }

    @Override
    public ShipmentResponse toShipmentResponse(Shipment shipment) {
        if ( shipment == null ) {
            return null;
        }

        ShipmentResponse shipmentResponse = new ShipmentResponse();

        shipmentResponse.setResult( toShipmentResponseStatus( shipment ) );
        shipmentResponse.setTrackingNumber( shipment.getTrackingCode() );

        return shipmentResponse;
    }
}
