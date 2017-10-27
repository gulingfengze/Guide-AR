package com.jwetherell.augmented_reality.data;

import java.util.List;

import com.jwetherell.augmented_reality.ui.Marker;

/**
 * 这个抽象类扩展到新的数据源。
 */
public abstract class DataSource {

    public abstract List<Marker> getMarkers();
}
