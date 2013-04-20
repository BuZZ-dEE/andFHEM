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

package li.klass.fhem.adapter.devices;

import android.content.Context;
import android.content.Intent;
import android.widget.TableLayout;
import android.widget.TableRow;
import li.klass.fhem.adapter.devices.core.FieldNameAddedToDetailListener;
import li.klass.fhem.adapter.devices.core.GenericDeviceAdapter;
import li.klass.fhem.adapter.devices.genericui.SeekBarActionRowFullWidthAndButton;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.domain.SonosPlayerDevice;

public class SonosPlayerAdapter extends GenericDeviceAdapter<SonosPlayerDevice> {
    public SonosPlayerAdapter() {
        super(SonosPlayerDevice.class);
    }

    @Override
    protected void afterPropertiesSet() {
        registerFieldListener("volume", new FieldNameAddedToDetailListener<SonosPlayerDevice>() {
            @Override
            public void onFieldNameAdded(Context context, TableLayout tableLayout, String field, SonosPlayerDevice device, TableRow fieldTableRow) {
                tableLayout.addView(new SeekBarActionRowFullWidthAndButton<SonosPlayerDevice>(context, Integer.valueOf(device.getVolume()), 100) {

                    @Override
                    public void onButtonSetValue(SonosPlayerDevice device, int value) {
                        onStopTrackingTouch(context, device, value);
                    }

                    @Override
                    public void onStopTrackingTouch(Context context, SonosPlayerDevice device, int progress) {
                        Intent intent = new Intent(Actions.DEVICE_SET_SUB_STATE);
                        intent.putExtra(BundleExtraKeys.DEVICE_NAME, device.getName());
                        intent.putExtra(BundleExtraKeys.STATE_NAME, "volume");
                        intent.putExtra(BundleExtraKeys.STATE_VALUE, progress + "");
                        putUpdateExtra(intent);

                        context.startService(intent);
                    }
                }.createRow(inflater, device));
            }
        });
    }
}
