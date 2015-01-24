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

package li.klass.fhem.activities.graph.additions;

import java.util.ArrayList;
import java.util.List;

import li.klass.fhem.activities.graph.ChartData;
import li.klass.fhem.activities.graph.ViewableChartSeries;
import li.klass.fhem.service.graph.GraphEntry;

/**
 * Class representing any additional charts for a given {@link ChartData}. This can concern regression or sum charts.
 */
public abstract class AdditionalChart {
    protected final ChartData originData;
    protected List<GraphEntry> data = new ArrayList<GraphEntry>();

    public AdditionalChart(ChartData originData) {
        this.originData = originData;
        calculateData();
    }

    protected abstract void calculateData();

    public String getName() {
        String nameSuffix = originData.getContext().getString(getNameSuffixStringId());
        return originData.getSeriesDescription().getColumnName() + " " + nameSuffix;
    }

    protected abstract int getNameSuffixStringId();

    public List<GraphEntry> getData() {
        return data;
    }

    public ChartData getOriginData() {
        return originData;
    }

    public abstract ViewableChartSeries.ChartType getChartType();
}