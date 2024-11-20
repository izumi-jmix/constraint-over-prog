package org.izumi.jmix.dgrid161editor.screen.ship;

import java.util.Objects;

import io.jmix.core.AccessManager;
import io.jmix.ui.accesscontext.UiEntityAttributeContext;
import io.jmix.ui.component.EntityPicker;
import io.jmix.ui.component.Field;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.data.meta.EntityValueSource;
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
    private AccessManager accessManager;

    @Autowired
    private EntityPicker<Port> currentPortField;

    @Subscribe("manufacturerField")
    public void onManufacturerFieldValueChange(final HasValue.ValueChangeEvent<Manufacturer> event) {
        final var value = event.getValue();
        setEditableConsideringAccess(currentPortField, Objects.nonNull(value));
    }

    private void setEditableConsideringAccess(Field<?> field, boolean editable) {
        if (!editable) {
            field.setEditable(false);
            return;
        }

        final var valueSource = field.getValueSource();
        if (!(valueSource instanceof EntityValueSource<?,?>)) {
            //unable to determine user access
            //log / throw exception
            return;
        }

        final var entityValueSource = (EntityValueSource<?, ?>) valueSource;
        final var propertyPath = entityValueSource.getMetaPropertyPath();

        final var attributeContext = new UiEntityAttributeContext(propertyPath);
        accessManager.applyRegisteredConstraints(attributeContext);
        if (attributeContext.canModify()) {
            field.setEditable(true);
        }
    }
}