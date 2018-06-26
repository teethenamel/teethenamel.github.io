package net.ramify.model.event;

import net.ramify.model.date.DateRange;
import net.ramify.model.place.address.Address;

public class Marriage extends AbstractPhysicalEvent {

    public Marriage(final DateRange date) {
        this(date, Address.UNKNOWN);
    }

    public Marriage(final DateRange date, final Address address) {
        this(date, "Marriage", address);
    }

    public Marriage(DateRange date, String description, Address address) {
        super(date, description, address);
    }

    @Override
    public boolean unique() {
        return false;
    }

}
