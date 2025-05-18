package homework1;

import java.util.Iterator;
/**
 * A RouteFormatter class knows how to create a textual description of
 * directions from one location to another. The class is abstract to
 * support different textual descriptions.
 */
public abstract class RouteFormatter {

  	/**
     * Give directions for following this Route, starting at its start point
     * and facing in the specified heading.
     * @requires route != null && 
     * 			0 <= heading < 360
     * @param route the route for which to print directions.
   	 * @param heading the initial heading.
     * @return A newline-terminated directions <tt>String</tt> giving
     * 	       human-readable directions from start to end along this route.
     **/
  	public String computeDirections(Route route, double heading) {
  		// Implementation hint:
		// This method should call computeLine() for each geographic
		// feature in this route and concatenate the results into a single
		// String.
  		if(route == null){
         throw new IllegalArgumentException("Route must not be null");
      }
      if(heading < 0 || heading >= 360) {
  			throw new IllegalArgumentException("heading must be between 0 and 360");
  		}
  		StringBuffer directions = new StringBuffer();
  		Iterator<GeoFeature> it = route.getGeoFeatures();
  		while(it.hasNext()) {
  			GeoFeature geoFeature = it.next();
  			directions.append(computeLine(geoFeature, heading));
  			heading = geoFeature.getEndHeading();
  			
  		}
  		return directions.toString();
  	}


  	/**
     * Computes a single line of a multi-line directions String that
     * represents the instructions for traversing a single geographic
     * feature.
     * @requires geoFeature != null
     * @param geoFeature the geographical feature to traverse.
   	 * @param origHeading the initial heading.
     * @return A newline-terminated <tt>String</tt> that gives directions
     * 		   on how to traverse this geographic feature.
     */
  	public abstract String computeLine(GeoFeature geoFeature, double origHeading);


  	/**
     * Computes directions to turn based on the heading change.
     * @requires 0 <= oldHeading < 360 &&
     *           0 <= newHeading < 360
     * @param origHeading the start heading.
   	 * @param newHeading the desired new heading.
     * @return English directions to go from the old heading to the new
     * 		   one. Let the angle from the original heading to the new
     * 		   heading be a. The turn should be annotated as:
     * <p>
     * <pre>
     * Continue             if a < 10
     * Turn slight right    if 10 <= a < 60
     * Turn right           if 60 <= a < 120
     * Turn sharp right     if 120 <= a < 179
     * U-turn               if 179 <= a
     * </pre>
     * and likewise for left turns.
     */
  	protected String getTurnString(double origHeading, double newHeading) {
  		if (origHeading < 0 || origHeading >= 360) {
  			throw new IllegalArgumentException("origHeading must be between 0 and 360");
  		}
  		if (newHeading < 0 || newHeading >= 360) {
  			throw new IllegalArgumentException("newHeading must be between 0 and 360");
  		}
  		double angle = Math.abs(newHeading - origHeading);
  		if (angle < 10) {
  			return "Continue";
  		} else if (angle < 60) {
  			return "Turn slight right";
  		} else if (angle < 120) {
  			return "Turn right";
  		} else if (angle < 179) {
  			return "Turn sharp right";
  		} else if (angle < 181) {
  			return "U-turn";
  		} else if (angle < 240) {
  			return "Turn sharp left";
  		} else if (angle < 300) {
  			return "Turn left";
  		} else if (angle < 350) {
  			return "Turn slight left";
  		} else {
  			return "Continue";
  		}
  	}

}
