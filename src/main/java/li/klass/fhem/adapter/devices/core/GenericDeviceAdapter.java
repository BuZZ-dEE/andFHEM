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

package li.klass.fhem.adapter.devices.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import li.klass.fhem.R;
import li.klass.fhem.adapter.devices.core.showFieldAnnotation.AnnotatedDeviceClassItem;
import li.klass.fhem.adapter.devices.genericui.DeviceDetailViewAction;
import li.klass.fhem.adapter.devices.genericui.HolderActionRow;
import li.klass.fhem.adapter.devices.genericui.WebCmdActionRow;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.domain.core.DeviceChart;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.domain.genericview.DetailViewSettings;
import li.klass.fhem.domain.genericview.OverviewViewSettings;
import li.klass.fhem.fhem.DataConnectionSwitch;
import li.klass.fhem.fhem.DummyDataConnection;
import li.klass.fhem.service.intent.DeviceIntentService;
import li.klass.fhem.util.StringUtil;

import static li.klass.fhem.adapter.devices.core.GenericDeviceOverviewViewHolder.GenericDeviceTableRowHolder;

public class GenericDeviceAdapter<D extends FhemDevice<D>> extends DeviceAdapter<D> {
    private static final String TAG = GenericDeviceAdapter.class.getName();
    protected List<DeviceDetailViewAction<D>> detailActions = new ArrayList<>();
    @Inject
    DataConnectionSwitch dataConnectionSwitch;
    private Class<D> deviceClass;
    private Map<String, List<FieldNameAddedToDetailListener<D>>> fieldNameAddedListeners = new HashMap<>();
    /**
     * Field to cache our sorted and annotated class members. This is especially useful as
     * recreating this list on each view creation is really really expensive (involves reflection,
     * list sorting, object creation, ...)
     */
    private transient List<AnnotatedDeviceClassItem> sortedAnnotatedClassItems;

    public GenericDeviceAdapter(Class<D> deviceClass) {
        super();

        this.deviceClass = deviceClass;
        afterPropertiesSet();
        registerFieldListener("state", new FieldNameAddedToDetailListener<D>() {
            @Override
            protected void onFieldNameAdded(Context context, TableLayout tableLayout, String field,
                                            D device, TableRow fieldTableRow) {
                createWebCmdTableRowIfRequired(getInflater(), tableLayout, device);
            }
        });
    }

    protected void afterPropertiesSet() {
    }

    protected void registerFieldListener(String fieldName, FieldNameAddedToDetailListener<D> listener) {
        if (!fieldNameAddedListeners.containsKey(fieldName)) {
            fieldNameAddedListeners.put(fieldName, new ArrayList<FieldNameAddedToDetailListener<D>>());
        }

        fieldNameAddedListeners.get(fieldName).add(listener);
    }

    private void createWebCmdTableRowIfRequired(LayoutInflater inflater, TableLayout layout,
                                                    final D device) {
        if (device.getWebCmd().isEmpty()) return;
        final Context context = inflater.getContext();

        layout.addView(new WebCmdActionRow<D>(HolderActionRow.LAYOUT_DETAIL, context)
                .createRow(context, inflater, layout, device));
    }

    public View createOverviewView(LayoutInflater layoutInflater, View convertView, FhemDevice rawDevice, long lastUpdate) {
        if (convertView == null || convertView.getTag() == null) {
            convertView = layoutInflater.inflate(getOverviewLayout(), null);
            GenericDeviceOverviewViewHolder viewHolder = new GenericDeviceOverviewViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.i(TAG, "Reusing generic device overview view");
        }
        GenericDeviceOverviewViewHolder viewHolder = (GenericDeviceOverviewViewHolder) convertView.getTag();
        fillDeviceOverviewView(convertView, (D) rawDevice, lastUpdate, viewHolder);
        return convertView;
    }

    private int getOverviewLayout() {
        return R.layout.device_overview_generic;
    }

    private void fillDeviceOverviewView(View view, D device, long lastUpdate, GenericDeviceOverviewViewHolder viewHolder) {
        viewHolder.resetHolder();
        setTextView(viewHolder.getDeviceName(), device.getAliasOrName());
        try {
            OverviewViewSettings annotation = device.getOverviewViewSettings();
            List<AnnotatedDeviceClassItem> items = getSortedAnnotatedClassItems(device);
            int currentGenericRow = 0;
            for (AnnotatedDeviceClassItem item : items) {
                String name = item.getName();
                boolean alwaysShow = false;
                if (annotation != null) {
                    if (name.equalsIgnoreCase("state")) {
                        if (!annotation.showState()) continue;
                        alwaysShow = true;
                    }

                    if (name.equalsIgnoreCase("measured")) {
                        if (!annotation.showMeasured()) continue;
                        alwaysShow = true;
                    }
                }
                if (alwaysShow || item.isShowInOverview()) {
                    currentGenericRow++;
                    GenericDeviceTableRowHolder rowHolder;
                    if (currentGenericRow <= viewHolder.getTableRowCount()) {
                        rowHolder = viewHolder.getTableRowAt(currentGenericRow - 1);
                    } else {
                        rowHolder = createTableRow(getInflater(), R.layout.device_overview_generic_table_row);
                        viewHolder.addTableRow(rowHolder);
                    }
                    fillTableRow(rowHolder, item, device);
                    viewHolder.getTableLayout().addView(rowHolder.row);
                }
            }

            if (isOverviewError(device, lastUpdate)) {
                Resources resources = getContext().getResources();
                int color = resources.getColor(R.color.errorBackground);
                view.setBackgroundColor(color);
            }

        } catch (Exception e) {
            Log.e(TAG, "exception occurred while setting device overview values", e);
        }
    }

    private List<AnnotatedDeviceClassItem> getSortedAnnotatedClassItems(D device) {
        if (sortedAnnotatedClassItems == null) {
            sortedAnnotatedClassItems = DeviceFields.getSortedAnnotatedClassItems(device.getClass());
        }
        return sortedAnnotatedClassItems;
    }

    private GenericDeviceTableRowHolder createTableRow(LayoutInflater inflater, int resource) {
        GenericDeviceTableRowHolder holder = new GenericDeviceTableRowHolder();
        TableRow tableRow = (TableRow) inflater.inflate(resource, null);
        assert tableRow != null;
        holder.row = tableRow;
        holder.description = (TextView) tableRow.findViewById(R.id.description);
        holder.value = (TextView) tableRow.findViewById(R.id.value);
        return holder;
    }

    protected boolean isOverviewError(D device, long lastUpdate) {
        // It does not make sense to show measure errors for data stemming out of a prestored
        // XML file.
        return !(dataConnectionSwitch.getCurrentProvider() instanceof DummyDataConnection) &&
                lastUpdate != -1 &&
                device.isSensorDevice() &&
                device.isOutdatedData(lastUpdate);

    }

    private void fillTableRow(GenericDeviceTableRowHolder holder, AnnotatedDeviceClassItem item, D device) {
        String value = item.getValueFor(device);
        int description = item.getDescriptionStringId();
        setTextView(holder.description, description);
        setTextView(holder.value, String.valueOf(value));
        if (value == null || value.equals("")) {
            holder.row.setVisibility(View.GONE);
        } else {
            holder.row.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean supportsDetailView(FhemDevice device) {
        return true;
    }

    @Override
    protected final View getDeviceDetailView(Context context, D device, long lastUpdate) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(getDetailViewLayout(), null);
        fillDeviceDetailView(context, view, device);

        if (device.isSensorDevice() && device.isOutdatedData(lastUpdate)) {
            View measureErrorView = view.findViewById(R.id.measure_error_notification);
            if (measureErrorView != null) {
                measureErrorView.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    @Override
    public int getDetailViewLayout() {
        return R.layout.device_detail_generic;
    }

    protected void fillDeviceDetailView(Context context, View view, D device) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TableLayout layout = (TableLayout) view.findViewById(R.id.generic);

        try {
            DetailViewSettings annotation = device.getClass().getAnnotation(DetailViewSettings.class);
            List<AnnotatedDeviceClassItem> items = getSortedAnnotatedClassItems(device);

            for (AnnotatedDeviceClassItem item : items) {
                String name = item.getName();

                if (annotation != null) {
                    if (name.equalsIgnoreCase("state") && !annotation.showState()) {
                        continue;
                    }

                    if (name.equalsIgnoreCase("measured") && !annotation.showMeasured()) {
                        continue;
                    }
                }

                if (item.isShowInDetail()) {
                    GenericDeviceTableRowHolder holder = createTableRow(inflater, R.layout.device_detail_generic_table_row);
                    fillTableRow(holder, item, device);
                    layout.addView(holder.row);
                    notifyFieldListeners(context, device, layout, name, holder.row);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "exception occurred while setting device overview values", e);
        }

        addDetailGraphButtons(context, view, device, inflater);

        addDetailActionButtons(context, view, device, inflater);

        LinearLayout otherStuffView = (LinearLayout) view.findViewById(R.id.otherStuff);
        fillOtherStuffDetailLayout(context, otherStuffView, device, inflater);

        updateGeneralDetailsNotificationText(context, view, device);
    }

    private void notifyFieldListeners(Context context, D device, TableLayout layout, String fieldName, TableRow fieldTableRow) {
        if (!fieldNameAddedListeners.containsKey(fieldName)) {
            return;
        }

        List<FieldNameAddedToDetailListener<D>> listeners = fieldNameAddedListeners.get(fieldName);
        for (FieldNameAddedToDetailListener<D> listener : listeners) {
            if (listener.supportsDevice(device)) {
                listener.onFieldNameAdded(context, layout, fieldName, device, fieldTableRow);
            }
        }
    }

    private void addDetailGraphButtons(Context context, View view, D device, LayoutInflater inflater) {
        LinearLayout graphLayout = (LinearLayout) view.findViewById(R.id.graphButtons);
        if (device.getDeviceCharts().size() == 0 || device.getLogDevices() == null) {
            graphLayout.setVisibility(View.GONE);
            return;
        }
        for (DeviceChart deviceChart : device.getDeviceCharts()) {
            addGraphButton(context, graphLayout, inflater, device, deviceChart);
        }
    }

    private void addDetailActionButtons(Context context, View view, D device, LayoutInflater inflater) {
        LinearLayout actionLayout = (LinearLayout) view.findViewById(R.id.actionButtons);
        if (!hasDetailActions(device)) {
            actionLayout.setVisibility(View.GONE);
        }
        for (DeviceDetailViewAction<D> action : detailActions) {
            if (!action.isVisible(device)) continue;

            View actionView = action.createView(context, inflater, device, actionLayout);
            actionLayout.addView(actionView);
        }
    }

    protected void fillOtherStuffDetailLayout(Context context, LinearLayout layout, D device, LayoutInflater inflater) {
    }

    private void updateGeneralDetailsNotificationText(Context context, View view, D device) {
        String text = getGeneralDetailsNotificationText(context, device);
        TextView notificationView = (TextView) view.findViewById(R.id.general_details_notification);

        if (StringUtil.isBlank(text)) {
            notificationView.setVisibility(View.GONE);
            return;
        }

        notificationView.setText(text);
        notificationView.setVisibility(View.VISIBLE);
    }

    private void addGraphButton(final Context context, LinearLayout graphLayout,
                                LayoutInflater inflater, final D device, final DeviceChart chart) {
        Button button = (Button) inflater.inflate(R.layout.button_device_detail, graphLayout, false);
        assert button != null;

        fillGraphButton(context, device, chart, button);
        graphLayout.addView(button);
    }

    private boolean hasDetailActions(D device) {
        if (detailActions.size() == 0) return false;

        for (DeviceDetailViewAction<D> detailAction : detailActions) {
            if (detailAction.isVisible(device)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class getOverviewViewHolderClass() {
        return GenericDeviceOverviewViewHolder.class;
    }

    protected String getGeneralDetailsNotificationText(Context context, D device) {
        return null;
    }

    @Override
    protected Intent onFillDeviceDetailIntent(Context context, FhemDevice device, Intent intent) {
        return intent;
    }

    @Override
    public Class<? extends FhemDevice> getSupportedDeviceClass() {
        return deviceClass;
    }

    protected void sendStateAction(Context context, D device, String action) {
        Intent intent = new Intent(Actions.DEVICE_SET_STATE);
        intent.setClass(context, DeviceIntentService.class);
        intent.putExtra(BundleExtraKeys.DEVICE_NAME, device.getName());
        intent.putExtra(BundleExtraKeys.DEVICE_TARGET_STATE, action);
        putUpdateExtra(intent);

        context.startService(intent);
    }

    public void putUpdateExtra(Intent intent) {
        intent.putExtra(BundleExtraKeys.RESULT_RECEIVER, new UpdatingResultReceiver(getContext()));
    }
}
