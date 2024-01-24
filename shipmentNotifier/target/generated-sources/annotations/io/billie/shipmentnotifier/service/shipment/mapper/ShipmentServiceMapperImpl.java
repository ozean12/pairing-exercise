package io.billie.shipmentnotifier.service.shipment.mapper;

import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-24T17:39:52+0330",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Private Build)"
)
@Component
public class ShipmentServiceMapperImpl implements ShipmentServiceMapper {

    @Override
    public Shipment toShipment(ShipmentDto shipmentDto, Order order, boolean isSuccessful) {
        if ( shipmentDto == null && order == null ) {
            return null;
        }

        Shipment shipment = new Shipment();

        if ( shipmentDto != null ) {
            shipment.setAmount( shipmentDto.getShipmentAmount() );
        }
        if ( order != null ) {
            shipment.setOrder( order );
            shipment.setId( order.getId() );
            shipment.setLastModifiedAt( order.getLastModifiedAt() );
            shipment.setCreatedAt( order.getCreatedAt() );
        }
        shipment.setStatus( toShipmentStatus( isSuccessful ) );
        shipment.setDescription( toShipmentDescription( isSuccessful ) );
        shipment.setTrackingCode( generateTrackingCode() );

        return shipment;
    }
}
