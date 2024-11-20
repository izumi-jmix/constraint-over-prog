package org.izumi.jmix.dgrid161editor.screen.ship;

import java.util.Objects;

import io.jmix.core.DataManager;
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
    private DataManager dataManager;

    @Autowired
    private EntityPicker<Port> currentPortField;

    @Subscribe
    public void onAfterShow(final AfterShowEvent event) {
        final var edited = getEditedEntity();
        if (Objects.isNull(edited.getManufacturer())) {
            dataManager.unconstrained()
                    .load(Manufacturer.class)
                    .query("e.name = :name")
                    .parameter("name", "default")
                    .optional()
                    .ifPresent(edited::setManufacturer);
        }
    }

    @Subscribe("manufacturerField")
    public void onManufacturerFieldValueChange(final HasValue.ValueChangeEvent<Manufacturer> event) {
        final var value = event.getValue();
        currentPortField.setEditable(Objects.nonNull(value));
    }
}