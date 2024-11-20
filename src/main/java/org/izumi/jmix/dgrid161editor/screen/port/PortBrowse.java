package org.izumi.jmix.dgrid161editor.screen.port;

import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.Install;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.izumi.jmix.dgrid161editor.entity.Port;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Port.browse")
@UiDescriptor("port-browse.xml")
@LookupComponent("portsTable")
public class PortBrowse extends StandardLookup<Port> {

    @Autowired
    private GroupTable<Port> portsTable;

    @Install(to = "portsTable.ownAction", subject = "enabledRule")
    private boolean portsTableOwnActionEnabledRule() {
        return portsTable.getSelected().size() == 2;
    }
}