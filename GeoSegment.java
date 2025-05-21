package homework1;

import java.util.Objects;

/**
 * A GeoSegment models a straight line segment on the earth. GeoSegments
 * are immutable.
 * <p>
 * A compass heading is a nonnegative real number less than 360. In compass
 * headings, north = 0, east = 90, south = 180, and west = 270.
 * <p>
 * When used in a map, a GeoSegment might represent part of a street,
 * boundary, or other feature.
 * As an example usage, this map
 * 
 * <pre>
 *  Trumpeldor   a
 *  Avenue       |
 *               i--j--k  Hanita
 *               |
 *               z
 * </pre>
 * 
 * could be represented by the following GeoSegments:
 * ("Trumpeldor Avenue", a, i), ("Trumpeldor Avenue", z, i),
 * ("Hanita", i, j), and ("Hanita", j, k).
 * </p>
 * 
 * </p>
 * A name is given to all GeoSegment objects so that it is possible to
 * differentiate between two GeoSegment objects with identical
 * GeoPoint endpoints. Equality between GeoSegment objects requires
 * that the names be equal String objects and the end points be equal
 * GeoPoint objects.
 * </p>
 *
 * <b>The following fields are used in the specification:</b>
 * 
 * <pre>
 *   name : String       // aname of the geographic feature identified
 *   p1 : GeoPoint       // first endpoint of the segment
 *   p2 : GeoPoint       // second endpoint of the segment
 *   length : real       // straight-line distance between p1 and p2, in kilometers
 *   heading : angle     // compass heading from p1 to p2, in degrees
 * </pre>
 **/
public class GeoSegment {
	final String name;
	final GeoPoint p1;
	final GeoPoint p2;
	final double length;
	final double heading;

	// Abs. Function: Represents a segment joining two Geo Points, its length, its direction and its name.
	// Rep. Invariant: name != null && p1 != null && p2 != null && heading >= 0 && heading < 360

	/**
	 * Constructs a new GeoSegment with the specified name and endpoints.
	 * 
	 * @requires name != null && p1 != null && p2 != null
	 * @effects constructs a new GeoSegment with the specified name and endpoints.
	 * 		if p1 and p2 are the same point, the length is 0 and the heading is 0.
	 **/
	public GeoSegment(String name, GeoPoint p1, GeoPoint p2) {
		if (name == null || p1 == null || p2 == null) {
			throw new IllegalArgumentException("Name and points cannot be null");
		}
		this.name = name;
		this.p1 = p1;
		this.p2 = p2;
		this.length = p1.distanceTo(p2);
		if (p1.equals(p2)) {
			this.heading = 0;
		}
		else {
			this.heading = p1.headingTo(p2);
		}
		checkRep();
	}

	void checkRep() {
		assert(name != null && p1 != null && p2 != null && heading >= 0 && heading < 360);
	}

	/**
	 * Returns a new GeoSegment like this one, but with its endpoints reversed.
	 * 
	 * @return a new GeoSegment gs such that gs.name = this.name
	 *         && gs.p1 = this.p2 && gs.p2 = this.p1
	 **/
	public GeoSegment reverse() {
		
		return new GeoSegment(name, p2, p1);
	}

	/**
	 * Returns the name of this GeoSegment.
	 * 
	 * @return the name of this GeoSegment.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns first endpoint of the segment.
	 * 
	 * @return first endpoint of the segment.
	 */
	public GeoPoint getP1() {
		return p1;
	}

	/**
	 * Returns second endpoint of the segment.
	 * 
	 * @return second endpoint of the segment.
	 */
	public GeoPoint getP2() {
		return p2;
	}

	/**
	 * Returns the length of the segment.
	 * 
	 * @return the length of the segment, using the flat-surface, near the
	 *         Technion approximation.
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Returns the compass heading from p1 to p2.
	 * 
	 * @requires this.length != 0
	 * @return the compass heading from p1 to p2, in degrees, using the
	 *         flat-surface, near the Technion approximation.
	 **/
	public double getHeading() {
		if (length == 0) {
			throw new IllegalStateException("Length is zero, cannot compute heading");
		}
		return heading;
	}

	/**
	 * Compares the specified Object with this GeoSegment for equality.
	 * 
	 * @return gs != null && (gs instanceof GeoSegment)
	 *         && gs.name = this.name && gs.p1 = this.p1 && gs.p2 = this.p2
	 **/
	public boolean equals(Object gs) {
		if (gs == null || !(gs instanceof GeoSegment)) {
			return false;
		}
		GeoSegment other = (GeoSegment) gs;
		return this.name.equals(other.name) && this.p1.equals(other.p1) && this.p2.equals(other.p2);
	}

	/**
	 * Returns a hash code value for this.
	 * 
	 * @return a hash code value for this.
	 **/
	public int hashCode() {
		// This implementation will work, but you may want to modify it
		// for improved performance.

		return Objects.hash(name, p1, p2);
	}

	/**
	 * Returns a string representation of this.
	 * 
	 * @return a string representation of this.
	 **/
	public String toString() {
		return  "(\"" + name + "\"," + p1 + ", " + p2 + ")";
	}

}
