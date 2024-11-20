package org.izumi.jmix.dgrid161editor.screen.ship;

import java.util.Objects;

import io.jmix.ui.component.EntityPicker;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Manufacturer;
import org.izumi.jmix.dgrid161editor.entity.Port;
import org.izumi.jmix.dgrid161editor.entity.Ship;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Ship.edit")
@UiDescriptor("ship-edit.xml")
@EditedEntityContainer("shipDc")
public class ShipEdit extends StandardEditor<Ship> {

    @Autowired
    private EntityPicker<Port> currentPortField;

    @Subscribe("manufacturerField")
    public void onManufacturerFieldValueChange(final HasValue.ValueChangeEvent<Manufacturer> event) {
        final var value = event.getValue();
        currentPortField.setEditable(Objects.nonNull(value));
    }
}