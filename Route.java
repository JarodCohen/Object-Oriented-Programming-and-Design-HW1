package homework1;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * <p>
 * Routes are immutable. New Routes can be constructed by adding a segment
 * to the end of a Route. An added segment must be properly oriented; that
 * is, its p1 field must correspond to the end of the original Route, and
 * its p2 field corresponds to the end of the new Route.
 * <p>
 * Because a Route is not necessarily straight, its length - the distance
 * traveled by following the path from start to end - is not necessarily
 * the same as the distance along a straight line between its endpoints.
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical features,
 * using the <tt>getGeoFeatures()</tt> method which returns an Iterator of
 * GeoFeature objects.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * 
 * <pre>
 *   start : GeoPoint            // location of the start of the route
 *   end : GeoPoint              // location of the end of the route
 *   startHeading : angle        // direction of travel at the start of the route, in degrees
 *   endHeading : angle          // direction of travel at the end of the route, in degrees
 *   geoFeatures : sequence      // a sequence of geographic features that make up this Route
 *   geoSegments : sequence      // a sequence of segments that make up this Route
 *   length : real               // total length of the route, in kilometers
 *   endingGeoSegment : GeoSegment  // last GeoSegment of the route
 * </pre>
 **/
public class Route {
   final GeoPoint start;
   final GeoPoint end;
   final double startHeading;
   final double endHeading;
   final List<GeoSegment> geoSegments;
   final List<GeoFeature> geoFeatures;
   final double length;
   final GeoSegment endingGeoSegment;
   // Implementation hint:
   // When asked to return an Iterator, consider using the iterator() method
   // in the List interface. Two nice classes that implement the List
   // interface are ArrayList and LinkedList. If comparing two Lists for
   // equality is needed, consider using the equals() method of List. More
   // info can be found at:
   // http://docs.oracle.com/javase/8/docs/api/java/util/List.html

   // Abs. Function: Represents a route composed of one or more segments connected to each others
   // by its start and end points, start and end headings, its length and  its last segment, and the lists of the segments and 
   // features that compose it.
   // Rep. Invariant: start != null && end != null && startHeading >= 0 && endHeading >= 0 &&
   // startHeading < 360 && endHeading < 360 && length >= 0 &&
   // geoSegments != null && geoFeatures != null && geoSegment[this.size(-1)] == endingGeoSegment && 
   // for each i < geoFeatures.size()-2 : geoFeatures.get(i).name != geoFeatures.get(i+1).name.

   void checkRep() {
      assert start != null;
      assert end != null;
      assert startHeading >= 0 && endHeading >= 0;
      assert startHeading < 360 && endHeading < 360;
      assert length >= 0;
      assert geoSegments != null && geoFeatures != null;
      assert geoSegments.get(geoSegments.size() - 1).equals(endingGeoSegment);
      for (int i = 0; i < geoFeatures.size() - 1; i++) {
         assert !geoFeatures.get(i).getName().equals(geoFeatures.get(i + 1).getName());
      }
   }
   /**
    * Constructs a new Route.
    * 
    * @requires gs != null
    * @effects Constructs a new Route, r, such that
    *          r.startHeading = gs.heading &&
    *          r.endHeading = gs.heading &&
    *          r.start = gs.p1 &&
    *          r.end = gs.p2
    **/
   public Route(GeoSegment gs) {
      if (gs == null) {
         throw new IllegalArgumentException("GeoSegment cannot be null");
      }
      this.start = gs.p1;
      this.end = gs.p2;
      this.startHeading = gs.heading;
      this.endHeading = gs.heading;
      this.length = gs.length;
      this.endingGeoSegment = gs;
      this.geoSegments = List.of(gs);
      this.geoFeatures = List.of(new GeoFeature(gs));
      checkRep();
   }

   private Route(GeoPoint start, GeoPoint end,
         double startHeading, double endHeading,
         double length, GeoSegment endingGeoSegment, List<GeoSegment> segments,
         List<GeoFeature> features) {
      this.start = start;
      this.end = end;
      this.startHeading = startHeading;
      this.endHeading = endHeading;
      this.endingGeoSegment = endingGeoSegment;
      this.length = length;
      this.geoSegments = List.copyOf(segments); // immutable
      this.geoFeatures = List.copyOf(features); // immutable
      checkRep();
   }

   /**
    * Returns location of the start of the route.
    * 
    * @return location of the start of the route.
    **/
   public GeoPoint getStart() {
      return start;
   }

   /**
    * Returns location of the end of the route.
    * 
    * @return location of the end of the route.
    **/
   public GeoPoint getEnd() {
      return end;
   }

   /**
    * Returns direction of travel at the start of the route, in degrees.
    * 
    * @return direction (in compass heading) of travel at the start of the
    *         route, in degrees.
    *         returns 0 if the first segment's length is 0.
    **/
   public double getStartHeading() {
      return startHeading;
   }

   /**
    * Returns direction of travel at the end of the route, in degrees.
    * 
    * @return direction (in compass heading) of travel at the end of the
    *         route, in degrees.
   *          returns 0 if the first segment's length is 0.
    **/
   public double getEndHeading() {
      return endHeading;
   }

   /**
    * Returns total length of the route.
    * 
    * @return total length of the route, in kilometers. NOTE: this is NOT
    *         as-the-crow-flies, but rather the total distance required to
    *         traverse the route. These values are not necessarily equal.
    **/
   public double getLength() {
      return length;
   }

   /**
    * Creates a new route that is equal to this route with gs appended to
    * its end.
    * 
    * @requires gs != null && gs.p1 == this.end
    * @return a new Route r such that
    *         r.end = gs.p2 &&
    *         r.endHeading = gs.heading &&
    *         r.length = this.length + gs.length
    **/
   public Route addSegment(GeoSegment gs) {
      if (gs == null) {
         throw new IllegalArgumentException("GeoSegment cannot be null");
      }
      // if (!this.end.equals(gs.p1) || !this.name.equals(gs.name)) {
      //    throw new IllegalArgumentException("GeoSegment does not match");
      // }
      if (!this.end.equals(gs.p1)) {
         throw new IllegalArgumentException("GeoSegment does not match");
      }

      List<GeoSegment> newSegments = new ArrayList<GeoSegment>(this.geoSegments);
      newSegments.add(gs);
      List<GeoFeature> newGeoFeatures;
      if (geoFeatures.get(geoFeatures.size()-1).getName().equals(gs.getName())) {
         newGeoFeatures = new ArrayList<GeoFeature>(this.geoFeatures);
         GeoFeature tmp = geoFeatures.get(geoFeatures.size()-1).addSegment(gs);
         newGeoFeatures.remove(geoFeatures.size()-1);
         newGeoFeatures.add(tmp);
      } else {
         newGeoFeatures = new ArrayList<GeoFeature>(this.geoFeatures);
         newGeoFeatures.add(new GeoFeature(gs));
      }
      checkRep();
      return new Route(
            this.start,
            gs.p2,
            this.startHeading,
            gs.heading,
            this.length + gs.length,
            gs,
            List.copyOf(newSegments),
            List.copyOf(newGeoFeatures));
   }

   /**
    * Returns an Iterator of GeoFeature objects. The concatenation
    * of the GeoFeatures, in order, is equivalent to this route. No two
    * consecutive GeoFeature objects have the same name.
    * 
    * @return an Iterator of GeoFeatures such that
    * 
    *         <pre>
    *      this.start        = a[0].start &&
    *      this.startHeading = a[0].startHeading &&
    *      this.end          = a[a.length - 1].end &&
    *      this.endHeading   = a[a.length - 1].endHeading &&
    *      this.length       = sum(0 <= i < a.length) . a[i].length &&
    *      for all integers i
    *          (0 <= i < a.length - 1 => (a[i].name != a[i+1].name &&
    *                                     a[i].end  == a[i+1].start))
    *         </pre>
    * 
    *         where <code>a[n]</code> denotes the nth element of the Iterator.
    * @see homework1.GeoFeature
    **/
   public Iterator<GeoFeature> getGeoFeatures() {
      return geoFeatures.iterator();
   }

   /**
    * Returns an Iterator of GeoSegment objects. The concatenation of the
    * GeoSegments, in order, is equivalent to this route.
    * 
    * @return an Iterator of GeoSegments such that
    * 
    *         <pre>
    *      this.start        = a[0].p1 &&
    *      this.startHeading = a[0].heading &&
    *      this.end          = a[a.length - 1].p2 &&
    *      this.endHeading   = a[a.length - 1].heading &&
    *      this.length       = sum (0 <= i < a.length) . a[i].length
    *         </pre>
    * 
    *         where <code>a[n]</code> denotes the nth element of the Iterator.
    * @see homework1.GeoSegment
    **/
   public Iterator<GeoSegment> getGeoSegments() {
      return geoSegments.iterator();
   }

   /**
    * Compares the specified Object with this Route for equality.
    * 
    * @return true iff (o instanceof Route) &&
    *         (o.geoFeatures and this.geoFeatures contain
    *         the same elements in the same order).
    **/
   public boolean equals(Object o) {
      if (o == null || !(o instanceof Route)) {
         return false;
      }
      Route other = (Route) o;
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
      // This implementation will work, but you may want to modify it
      // for improved performance.

      return 1;
   }

   /**
    * Returns a string representation of this.
    * 
    * @return a string representation of this.
    **/
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Route [start=").append(start).append(", end=").append(end)
            .append(", startHeading=").append(startHeading).append(", endHeading=")
            .append(endHeading).append(", length=").append(length)
            .append(", endingGeoSegment=").append(endingGeoSegment)
            .append(", geoSegments=").append(geoSegments).append("]");
      return sb.toString();
   }

}
