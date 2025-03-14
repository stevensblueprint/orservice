package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.model.ServiceAtLocation;
import jakarta.persistence.criteria.Path;
import org.apache.lucene.util.SloppyMath;
import org.springframework.data.jpa.domain.Specification;

public class ServiceAtLocationSpecifications {
  public static Specification<ServiceAtLocation> hasSearchTerm(String term) {
    return (root, query, criteriaBuilder) -> {
      String likeTerm = "%" + term + "%";
      return criteriaBuilder.or(
          criteriaBuilder.like(root.get("description"), likeTerm));
    };
  }

  public static Specification<ServiceAtLocation> hasPostalCode(String postcode) {
    return (root, query, criteriaBuilder) -> {
      // todo check against SAL.Location.Address.postal_code
      // todo make sure frontend provides postcode in the proper format - maybe do `contains` depending on format
      return criteriaBuilder.equal(root.get(""), postcode);
    };
  }

  // todo this would be nice for filtering the query as it happens, but it doesn't seem possible
  public static Specification<ServiceAtLocation> aroundLatLng(double lat, double lng, double meters) {
    return (root, query, criteriaBuilder) -> {
      // calculate Haversine distance in meters from the specified latlng coordinates
      Path<Location> otherLoc = root.get(""); // todo get SAL.Location.lat/lon
      // double distance = SloppyMath.haversinMeters(lat, lng, Double.parseDouble(otherLoc.get("latitude")), Double.parseDouble(otherLoc.get("longitude")));
      // return criteriaBuilder.lessThanOrEqualTo(distance, meters);
      return criteriaBuilder.conjunction(); // TODO REMOVE THIS
    };
  }
}
