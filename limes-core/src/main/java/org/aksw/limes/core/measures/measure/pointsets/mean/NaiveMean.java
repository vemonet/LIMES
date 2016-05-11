/**
 * 
 */
package org.aksw.limes.core.measures.measure.pointsets.mean;

import org.aksw.limes.core.datastrutures.Point;
import org.aksw.limes.core.io.cache.Instance;
import org.aksw.limes.core.io.mapping.Mapping;
import org.aksw.limes.core.io.mapping.MemoryMapping;
import org.aksw.limes.core.measures.mapper.atomic.OrchidMapper;
import org.aksw.limes.core.measures.mapper.atomic.hausdorff.Polygon;
import org.aksw.limes.core.measures.measure.pointsets.PointsetsMeasure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sherif
 *
 */
public class NaiveMean extends PointsetsMeasure {

	public int computations;

	/**
	 * Brute force approach to computing the mean distance between two polygons
	 *
	 * @param X First polygon
	 * @param Y Second polygon
	 * @return Distance between the two polygons
	 */
	public NaiveMean() {
		computations = 0;
	}


	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.pointsets.IPointsetsMeasure#computeDistance(org.aksw.limes.core.measures.mapper.atomic.hausdorff.Polygon, org.aksw.limes.core.measures.mapper.atomic.hausdorff.Polygon, double)
	 */
	public double computeDistance(Polygon X, Polygon Y, double threshold) {
		Point meanX = new Point();
		for (Point x : X.points) {
			meanX = Point.add(meanX, x);
		}
		for (int i = 0; i < meanX.coordinates.size(); i++) {
			meanX.coordinates.set(i, meanX.coordinates.get(i) / X.points.size());
		}
		Point meanY = new Point();
		for (Point y : Y.points) {
			meanY = Point.add(meanY, y);
		}
		for (int i = 0; i < meanY.coordinates.size(); i++) {
			meanY.coordinates.set(i, meanY.coordinates.get(i) / (double) Y.points.size());
		}
		return pointToPointDistance(meanX, meanY);
	}

	/**
	 * @param X Polygon
	 * @param Y Polygon
	 * @param threshold
	 * @return the mean distance between X and Y
	 */
	public static double distance(Polygon X, Polygon Y, double threshold) {
		Point meanX = new Point("meanX", new ArrayList<Double>());
		for (Point x : X.points) {
			meanX = Point.add(meanX, x);
		}
		for (int i = 0; i < meanX.coordinates.size(); i++) {
			meanX.coordinates.set(i, meanX.coordinates.get(i) / X.points.size());
		}

		Point meanY = new Point("meanY", new ArrayList<Double>());
		for (Point y : Y.points) {
			meanX = Point.add(meanY, y);
		}
		for (int i = 0; i < meanY.coordinates.size(); i++) {
			meanX.coordinates.set(i, meanY.coordinates.get(i) / Y.points.size());
		}

		return pointToPointDistance(meanX, meanY);
	}

	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.IMeasure#getName()
	 */
	public String getName() {
		return "naiveMean";
	}

	/**
	 * Computes the SetMeasure distance for a source and target set
	 *
	 * @param source Source polygons
	 * @param target Target polygons
	 * @param threshold Distance threshold
	 * @return Mapping of resources from Source to Target
	 */
	public Mapping run(Set<Polygon> source, Set<Polygon> target, double threshold) {
		Mapping m = new MemoryMapping();
		for (Polygon s : source) {
			for (Polygon t : target) {
				double d = computeDistance(s, t, threshold);

				if (d <= threshold) {
					m.add(s.uri, t.uri, d);
				}
			}
		}
		return m;
	}

	
	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.IMeasure#getSimilarity(java.lang.Object, java.lang.Object)
	 */
	public double getSimilarity(Object a, Object b) {
		Polygon p1 = OrchidMapper.getPolygon((String) a);
		Polygon p2 = OrchidMapper.getPolygon((String) b);
		double d = computeDistance(p1, p2, 0f);
		return 1d / (1d + (double) d);
	}

	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.IMeasure#getType()
	 */
	public String getType() {
		return "geodistance";
	}

	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.IMeasure#getSimilarity(org.aksw.limes.core.io.cache.Instance, org.aksw.limes.core.io.cache.Instance, java.lang.String, java.lang.String)
	 */
	public double getSimilarity(Instance a, Instance b, String property1, String property2) {
		TreeSet<String> source = a.getProperty(property1);
		TreeSet<String> target = b.getProperty(property2);
		Set<Polygon> sourcePolygons = new HashSet<Polygon>();
		Set<Polygon> targetPolygons = new HashSet<Polygon>();
		for (String s : source) {
			sourcePolygons.add(OrchidMapper.getPolygon(s));
		}
		for (String t : target) {
			targetPolygons.add(OrchidMapper.getPolygon(t));
		}
		double min = Double.MAX_VALUE;
		double d = 0;
		for (Polygon p1 : sourcePolygons) {
			for (Polygon p2 : targetPolygons) {
				d = computeDistance(p1, p2, 0);
				if (d < min) {
					min = d;
				}
			}
		}
		return 1d / (1d + (double) d);
	}

	/* (non-Javadoc)
	 * @see org.aksw.limes.core.measures.measure.IMeasure#getRuntimeApproximation(double)
	 */
	public double getRuntimeApproximation(double mappingSize) {
		return mappingSize / 1000d;
	}

}
