package org.izumi.jmix.dgrid161editor.screen.ship;

import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Ship;

@UiController("Ship.browse")
@UiDescriptor("ship-browse.xml")
@LookupComponent("shipsTable")
public class ShipBrowse extends StandardLookup<Ship> {
}