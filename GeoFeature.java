package homework1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A GeoFeature represents a route from one location to another along a
 * single geographic feature. GeoFeatures are immutable.
 * <p>
 * GeoFeature abstracts over a sequence of GeoSegments, all of which have
 * the same name, thus providing a representation for nonlinear or nonatomic
 * geographic features. As an example, a GeoFeature might represent the
 * course of a winding river, or travel along a road through intersections
 * but remaining on the same road.
 * <p>
 * GeoFeatures are immutable. New GeoFeatures can be constructed by adding
 * a segment to the end of a GeoFeature. An added segment must be properly
 * oriented; that is, its p1 field must correspond to the end of the original
 * GeoFeature, and its p2 field corresponds to the end of the new GeoFeature,
 * and the name of the GeoSegment being added must match the name of the
 * existing GeoFeature.
 * <p>
 * Because a GeoFeature is not necessarily straight, its length - the
 * distance traveled by following the path from start to end - is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * 
 * <pre>
 *   start : GeoPoint       // location of the start of the geographic feature
 *   end : GeoPoint         // location of the end of the geographic feature
 *   startHeading : angle   // direction of travel at the start of the geographic feature, in degrees
 *   endHeading : angle     // direction of travel at the end of the geographic feature, in degrees
 *   geoSegments : sequence	// a sequence of segments that make up this geographic feature
 *   name : String          // name of geographic feature
 *   length : real          // total length of the geographic feature, in kilometers
 * </pre>
 **/
public class GeoFeature {
	final GeoPoint start;
	final GeoPoint end;
	final double startHeading;
	final double endHeading;
	final String name;
	final double length;
	final List<GeoSegment> geoSegments;
	// Implementation hint:
	// When asked to return an Iterator, consider using the iterator() method
	// in the List interface. Two nice classes that implement the List
	// interface are ArrayList and LinkedList. If comparing two Lists for
	// equality is needed, consider using the equals() method of List. More
	// info can be found at:
	// http://docs.oracle.com/javase/8/docs/api/java/util/List.html

	// TODO Write abstraction function and representation invariant

	/**
	 * Constructs a new GeoFeature.
	 * 
	 * @requires gs != null
	 * @effects Constructs a new GeoFeature, r, such that
	 *          r.name = gs.name &&
	 *          r.startHeading = gs.heading &&
	 *          r.endHeading = gs.heading &&
	 *          r.start = gs.p1 &&
	 *          r.end = gs.p2
	 **/
	public GeoFeature(GeoSegment gs) {
		if (gs == null) {
			throw new IllegalArgumentException("GeoSegment cannot be null");
		}
		this.start = gs.p1;
		this.end = gs.p2;
		this.startHeading = gs.heading;
		this.endHeading = gs.heading;
		this.name = gs.name;
		this.length = gs.length;
		this.geoSegments = List.of(gs);
	}

	/**
	 * other constructor of Geofeature.
	 * 
	 * @effects Constructs a new GeoFeature, r, such that
	 *          r.name = gs.name &&
	 *          r.startHeading = start.heading &&
	 *          r.endHeading = end.heading &&
	 *          r.start = start &&
	 *          r.end = end
	 */
	private GeoFeature(GeoPoint start, GeoPoint end,
			double startHeading, double endHeading, String name,
			double length, List<GeoSegment> segments) {
		this.start = start;
		this.end = end;
		this.startHeading = startHeading;
		this.endHeading = endHeading;
		this.name = name;
		this.length = length;
		this.geoSegments = List.copyOf(segments); // Immutable copy
	}

	/**
	 * Returns name of geographic feature.
	 * 
	 * @return name of geographic feature
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns location of the start of the geographic feature.
	 * 
	 * @return location of the start of the geographic feature.
	 */
	public GeoPoint getStart() {
		return start;
	}

	/**
	 * Returns location of the end of the geographic feature.
	 * 
	 * @return location of the end of the geographic feature.
	 */
	public GeoPoint getEnd() {
		return end;
	}

	/**
	 * Returns direction of travel at the start of the geographic feature.
	 * 
	 * @return direction (in standard heading) of travel at the start of the
	 *         geographic feature, in degrees.
	 */
	public double getStartHeading() {
		return startHeading;
	}

	/**
	 * Returns direction of travel at the end of the geographic feature.
	 * 
	 * @return direction (in standard heading) of travel at the end of the
	 *         geographic feature, in degrees.
	 */
	public double getEndHeading() {
		return endHeading;
	}

	/**
	 * Returns total length of the geographic feature, in kilometers.
	 * 
	 * @return total length of the geographic feature, in kilometers.
	 *         NOTE: this is NOT as-the-crow-flies, but rather the total
	 *         distance required to traverse the geographic feature. These
	 *         values are not necessarily equal.
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Creates a new GeoFeature that is equal to this GeoFeature with gs
	 * appended to its end.
	 * 
	 * @requires gs != null && gs.p1 = this.end && gs.name = this.name.
	 * @return a new GeoFeature r such that
	 *         r.end = gs.p2 &&
	 *         r.endHeading = gs.heading &&
	 *         r.length = this.length + gs.length
	 **/
	public GeoFeature addSegment(GeoSegment gs) {
		if (gs == null) {
			throw new IllegalArgumentException("GeoSegment cannot be null");
		}
		if (!this.end.equals(gs.p1) || !this.name.equals(gs.name)) {
			throw new IllegalArgumentException("GeoSegment does not match");
		}

		// Create a new list of segments including the new one
		List<GeoSegment> newSegments = new ArrayList<GeoSegment>(this.geoSegments);
		newSegments.add(gs);
		newSegments = List.copyOf(newSegments); // Immutable copy
		// Create and return a new GeoFeature instance
		return new GeoFeature(
				this.start, // start remains the same
				gs.p2, // end is the end of the new segment
				this.startHeading, // start heading remains the same
				gs.heading, // end heading is the heading of the new segment
				this.name, // name remains the same
				this.length + gs.length, // total length is updated
				newSegments);
	}

	/**
	 * Returns an Iterator of GeoSegment objects. The concatenation of the
	 * GeoSegments, in order, is equivalent to this GeoFeature. All the
	 * GeoSegments have the same name.
	 * 
	 * @return an Iterator of GeoSegments such that
	 * 
	 *         <pre>
	 *      this.start        = a[0].p1 &&
	 *      this.startHeading = a[0].heading &&
	 *      this.end          = a[a.length - 1].p2 &&
	 *      this.endHeading   = a[a.length - 1].heading &&
	 *      this.length       = sum(0 <= i < a.length) . a[i].length &&
	 *      for all integers i
	 *          (0 <= i < a.length-1 => (a[i].name == a[i+1].name &&
	 *                                   a[i].p2d  == a[i+1].p1))
	 *         </pre>
	 * 
	 *         where <code>a[n]</code> denotes the nth element of the Iterator.
	 * @see homework1.GeoSegment
	 */
	public Iterator<GeoSegment> getGeoSegments() {
		// TODO Implement this method
		return geoSegments.iterator();
	}

	/**
	 * Compares the argument with this GeoFeature for equality.
	 * 
	 * @return o != null && (o instanceof GeoFeature) &&
	 *         (o.geoSegments and this.geoSegments contain
	 *         the same elements in the same order).
	 **/
	public boolean equals(Object o) {
		if (o == null || !(o instanceof GeoFeature)) {
			return false;
		}
		GeoFeature other = (GeoFeature) o;
		if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.geoSegments.size() != other.geoSegments.size()) {
			return false;
		}
		for (int i = 0; i < this.geoSegments.size(); i++) {
			if (!this.geoSegments.get(i).equals(other.geoSegments.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a hash code for this.
	 * 
	 * @return a hash code for this.
	 **/
	public int hashCode() {

		return 1;
	}

	/**
	 * Returns a string representation of this.
	 * 
	 * @return a string representation of this.
	 **/
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("GeoFeature [name=").append(name).append(", start=").append(start)
				.append(", end=").append(end).append(", startHeading=").append(startHeading)
				.append(", endHeading=").append(endHeading).append(", length=").append(length)
				.append(", geoSegments=").append(geoSegments).append("]");
		return sb.toString();
	}
}
