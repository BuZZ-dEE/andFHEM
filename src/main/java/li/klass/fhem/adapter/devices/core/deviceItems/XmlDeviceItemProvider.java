/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2011, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */

package li.klass.fhem.adapter.devices.core.deviceItems;

import com.google.common.base.Optional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import li.klass.fhem.domain.core.DeviceType;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.resources.ResourceIdMapper;
import li.klass.fhem.service.deviceConfiguration.DeviceConfiguration;
import li.klass.fhem.service.deviceConfiguration.DeviceConfiguration.ViewItemConfig;
import li.klass.fhem.service.deviceConfiguration.DeviceDescMapping;
import li.klass.fhem.service.room.xmllist.DeviceNode;
import li.klass.fhem.service.room.xmllist.XmlListDevice;

import static com.google.common.collect.Sets.newHashSet;

@Singleton
public class XmlDeviceItemProvider {

    @Inject
    DeviceDescMapping deviceDescMapping;

    @Inject
    public XmlDeviceItemProvider() {
    }

    public Set<DeviceViewItem> getDeviceClassItems(FhemDevice fhemDevice) {
        Set<DeviceViewItem> items = newHashSet();
        XmlListDevice xmlListDevice = fhemDevice.getXmlListDevice();

        if (xmlListDevice == null) return items;

        DeviceType deviceType = DeviceType.getDeviceTypeFor(xmlListDevice.getType());
        Optional<DeviceConfiguration> configuration = fhemDevice.getDeviceConfiguration();

        boolean showAll = !configuration.isPresent() && deviceType == DeviceType.GENERIC;
        items.addAll(statesFor(xmlListDevice, configuration, showAll));
        items.addAll(attributesFor(xmlListDevice, configuration, showAll));

        return items;
    }

    public Set<DeviceViewItem> getStatesFor(FhemDevice device, boolean showUnknown) {
        Optional<DeviceConfiguration> configuration = device.getDeviceConfiguration();
        return statesFor(device.getXmlListDevice(), configuration, showUnknown);
    }

    public Set<DeviceViewItem> getAttributesFor(FhemDevice device, boolean showUnknown) {
        Optional<DeviceConfiguration> configuration = device.getDeviceConfiguration();
        return attributesFor(device.getXmlListDevice(), configuration, showUnknown);
    }

    public Set<DeviceViewItem> getInternalsFor(FhemDevice device, boolean showUnknown) {
        Optional<DeviceConfiguration> configuration = device.getDeviceConfiguration();
        return internalsFor(device.getXmlListDevice(), configuration, showUnknown);
    }

    private Set<DeviceViewItem> statesFor(XmlListDevice device, Optional<DeviceConfiguration> config, boolean showUnknown) {
        Set<ViewItemConfig> configs = config.isPresent() ? config.get().getStates() : Collections.<ViewItemConfig>emptySet();
        Map<String, DeviceNode> deviceStates = device.getStates();

        return itemsFor(configs, deviceStates, showUnknown);
    }

    private Set<DeviceViewItem> attributesFor(XmlListDevice device, Optional<DeviceConfiguration> config, boolean showUnknown) {
        Set<ViewItemConfig> configs = config.isPresent() ? config.get().getAttributes() : Collections.<ViewItemConfig>emptySet();
        return itemsFor(configs, device.getAttributes(), showUnknown);
    }


    private Set<DeviceViewItem> internalsFor(XmlListDevice device, Optional<DeviceConfiguration> config, boolean showUnknown) {
        Set<ViewItemConfig> configs = config.isPresent() ? config.get().getInternals() : Collections.<ViewItemConfig>emptySet();
        return itemsFor(configs, device.getInternals(), showUnknown);
    }

    private Set<DeviceViewItem> itemsFor(Set<ViewItemConfig> configs, Map<String, DeviceNode> nodes, boolean showUnknown) {
        Set<DeviceViewItem> items = newHashSet();

        for (Map.Entry<String, DeviceNode> entry : nodes.entrySet()) {
            Optional<ViewItemConfig> config = configFor(configs, entry.getKey());
            if (config.isPresent()) {
                items.add(itemFor(config.get(), entry.getValue()));
            } else if (showUnknown) {
                items.add(genericItemFor(entry.getValue()));
            }
        }

        return items;
    }

    private Optional<ViewItemConfig> configFor(Set<ViewItemConfig> viewItemConfigs, String key) {
        for (ViewItemConfig config : viewItemConfigs) {
            if (config.getKey().equalsIgnoreCase(key)) {
                return Optional.of(config);
            }
        }
        return Optional.absent();
    }

    private DeviceViewItem genericItemFor(DeviceNode deviceNode) {
        String desc = deviceDescMapping.descFor(deviceNode.getKey());

        return new XmlDeviceViewItem(deviceNode.getKey(), desc,
                deviceNode.getValue(), null, true, false);
    }

    private XmlDeviceViewItem itemFor(ViewItemConfig config, DeviceNode deviceNode) {
        String jsonDesc = config.getDesc();
        String desc = jsonDesc != null ?
                deviceDescMapping.descFor(ResourceIdMapper.valueOf(jsonDesc)) :
                deviceDescMapping.descFor(deviceNode.getKey());

        String showAfter = config.getShowAfter() != null ? config.getShowAfter() : DeviceViewItem.FIRST;
        return new XmlDeviceViewItem(config.getKey(), desc,
                deviceNode.getValue(), showAfter, config.isShowInDetail(), config.isShowInOverview());
    }
}
