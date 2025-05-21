package homework1;

import java.util.Objects;

/**
 * A GeoPoint is a point on the earth. GeoPoints are immutable.
 * <p>
 * North latitudes and east longitudes are represented by positive numbers.
 * South latitudes and west longitudes are represented by negative numbers.
 * <p>
 * The code may assume that the represented points are nearby the Technion.
 * <p>
 * <b>Implementation direction</b>:<br>
 * The Ziv square is at approximately 32 deg. 46 min. 59 sec. N
 * latitude and 35 deg. 0 min. 52 sec. E longitude. There are 60 minutes
 * per degree, and 60 seconds per minute. So, in decimal, these correspond
 * to 32.783098 North latitude and 35.014528 East longitude. The
 * constructor takes integers in millionths of degrees. To create a new
 * GeoPoint located in the the Ziv square, use:
 * <tt>GeoPoint zivCrossroad = new GeoPoint(32783098,35014528);</tt>
 * <p>
 * Near the Technion, there are approximately 110.901 kilometers per degree
 * of latitude and 93.681 kilometers per degree of longitude. An
 * implementation may use these values when determining distances and
 * headings.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * 
 * <pre>
 *   latitude :  real        // latitude measured in degrees
 *   longitude : real        // longitude measured in degrees
 * </pre>
 **/
public class GeoPoint {
	final int latitude;
	final int longitude;

	/** Minimum value the latitude field can have in this class. **/
	public static final int MIN_LATITUDE = -90 * 1000000;

	/** Maximum value the latitude field can have in this class. **/
	public static final int MAX_LATITUDE = 90 * 1000000;

	/** Minimum value the longitude field can have in this class. **/
	public static final int MIN_LONGITUDE = -180 * 1000000;

	/** Maximum value the longitude field can have in this class. **/
	public static final int MAX_LONGITUDE = 180 * 1000000;

	/**
	 * Approximation used to determine distances and headings using a
	 * "flat earth" simplification.
	 */
	public static final double KM_PER_DEGREE_LATITUDE = 110.901;

	/**
	 * Approximation used to determine distances and headings using a
	 * "flat earth" simplification.
	 */
	public static final double KM_PER_DEGREE_LONGITUDE = 93.681;

	// Implementation hint:
	// Doubles and floating point math can cause some problems. The exact
	// value of a double can not be guaranteed except within some epsilon.
	// Because of this, using doubles for the equals() and hashCode()
	// methods can have erroneous results. Do not use floats or doubles for
	// any computations in hashCode(), equals(), or where any other time
	// exact values are required. (Exact values are not required for length
	// and distance computations). Because of this, you should consider
	// using ints for your internal representation of GeoPoint.

	// Abs. Function: {p.latitude, p.longitude} where p is a GeoPoint object
	//Rep. Invariant:(MIN_LATITUDE <= p.latitude <= MAX_LATITUDE) &&
	//         (MIN_LONGITUDE <= p.longitude <= MAX_LONGITUDE)

	/**
	 * Constructs GeoPoint from a latitude and longitude.
	 * 
	 * @requires the point given by (latitude, longitude) in millionths
	 *           of a degree is valid such that:
	 *           (MIN_LATITUDE <= latitude <= MAX_LATITUDE) and
	 *           (MIN_LONGITUDE <= longitude <= MAX_LONGITUDE)
	 * @effects constructs a GeoPoint from a latitude and longitude
	 *          given in millionths of degrees.
	 **/
	public GeoPoint(int latitude, int longitude) {
		GeoPoint.checkLatitude(latitude);
		GeoPoint.checkLongitude(longitude);
		this.latitude = latitude;
		this.longitude = longitude;
		checkRep();
	}

	/**
	 * Checks that the given latitude is valid.
	 * 
	 * @requires latitude is in millionths of degrees
	 * @effects throws IllegalArgumentException if latitude is not valid
	 **/
	private static void checkLatitude(int latitude) {
		if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
			throw new IllegalArgumentException("Invalid latitude: " + latitude);
		}
	}

	/**
	 * Checks that the given longitude is valid.
	 * 
	 * @requires longitude is in millionths of degrees
	 * @effects throws IllegalArgumentException if longitude is not valid
	 **/
	private static void checkLongitude(int longitude) {
		if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
			throw new IllegalArgumentException("Invalid longitude: " + longitude);
		}
	}

	/**
	 * Checks the representation invariant.
	 * 
	 * @effects throws IllegalStateException if the representation
	 *          invariant is violated
	 **/
	private void checkRep() {
		assert(latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE);
		
		assert(longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE);
	}

	// }
	/**
	 * Returns the latitude of this.
	 * 
	 * @return the latitude of this in millionths of degrees.
	 */
	public int getLatitude() {
		return latitude;
	}

	/**
	 * Returns the longitude of this.
	 * 
	 * @return the latitude of this in millionths of degrees.
	 */
	public int getLongitude() {
		return longitude;
	}

	/**
	 * Computes the distance between GeoPoints.
	 * 
	 * @requires gp != null
	 * @return the distance from this to gp, using the flat-surface, near
	 *         the Technion approximation.
	 **/
	public double distanceTo(GeoPoint gp) {
		if (gp == null) {
			throw new IllegalArgumentException("GeoPoint must not be null");
		}

		double lat1 = this.latitude / 1_000_000.0;
		double lon1 = this.longitude / 1_000_000.0;
		double lat2 = gp.latitude / 1_000_000.0;
		double lon2 = gp.longitude / 1_000_000.0;

		double dy = (lat2 - lat1) * KM_PER_DEGREE_LATITUDE;
		double dx = (lon2 - lon1) * KM_PER_DEGREE_LONGITUDE;

		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Computes the compass heading between GeoPoints.
	 * 
	 * @requires gp != null && !this.equals(gp)
	 * @return the compass heading h from this to gp, in degrees, using the
	 *         flat-surface, near the Technion approximation, such that
	 *         0 <= h < 360. In compass headings, north = 0, east = 90,
	 *         south = 180, and west = 270.
	 **/
	public double headingTo(GeoPoint gp) {
		if (gp == null || this.equals(gp)) {
			throw new IllegalArgumentException("GeoPoint must not be null");
		}
		int dx = gp.longitude - this.longitude;
		int dy = gp.latitude - this.latitude;
		double angleRad = Math.atan2(dx, dy); // dx first for compass convention
		double angleDeg = Math.toDegrees(angleRad);
		if (angleDeg < 0) {
			angleDeg += 360;
		}
		return angleDeg;
		// Implementation hints:
		// 1. You may find the mehtod Math.atan2() useful when
		// implementing this method. More info can be found at:
		// http://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
		//
		// 2. Keep in mind that in our coordinate system, north is 0
		// degrees and degrees increase in the clockwise direction. By
		// mathematical convention, "east" is 0 degrees, and degrees
		// increase in the counterclockwise direction.

	}

	/**
	 * Compares the specified Object with this GeoPoint for equality.
	 * 
	 * @return gp != null && (gp instanceof GeoPoint) &&
	 *         gp.latitude = this.latitude && gp.longitude = this.longitude
	 **/
	public boolean equals(Object gp) {
		if (gp == null || !(gp instanceof GeoPoint)) {
			return false;
		}
		GeoPoint other = (GeoPoint) gp;
		return this.latitude == other.latitude && this.longitude == other.longitude;
	}

	/**
	 * Returns a hash code value for this GeoPoint.
	 * 
	 * @return a hash code value for this GeoPoint.
	 **/
	public int hashCode() {
		// This implementation will work, but you may want to modify it
		// for improved performance.
		return Objects.hash(latitude, longitude);
	}

	/**
	 * Returns a string representation of this GeoPoint.
	 * 
	 * @return a string representation of this GeoPoint.
	 **/
	public String toString() {
		return "(" + latitude + ", " + longitude + ")";
	}

}
